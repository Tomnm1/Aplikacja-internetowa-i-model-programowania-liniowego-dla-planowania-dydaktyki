import React, {useEffect, useState} from 'react';
import TeacherPreferences from "./TeacherPreferences";
import {BackendTeacher, SlotPreference, SubjectType} from "../utils/Interfaces";
import {getTeacher, updateTeacher} from "../app/slices/teacherSlice";
import {useAppDispatch, useAppSelector} from "../hooks/hooks";
import {RootState} from "../app/store";
import {useSnackbar} from "notistack";
import ActionButton from "../utils/ActionButton";
import SaveIcon from "@mui/icons-material/Save";
import {CircularProgress} from "@mui/material";

interface TeacherFormData {
    id: number;
    firstName: string;
    lastName: string;
    degree: string;
    subjectTypesList: SubjectType[];
}

const UserDesiderata: React.FC = () => {
    const dispatch = useAppDispatch();
    const {enqueueSnackbar} = useSnackbar();
    const {userId, isAuthenticated, role} = useAppSelector((state: RootState) => state.auth);
    const teacher = useAppSelector((state: RootState) => state.teachers.singleTeacher);
    const teacherLoading = useAppSelector((state: RootState) => state.teachers.loading);
    const teacherError = useAppSelector((state: RootState) => state.teachers.error);

    const [preferences, setPreferences] = useState<Record<number, SlotPreference>>({});
    const [formData, setFormData] = useState<TeacherFormData>({
        id: 0, firstName: '', lastName: '', degree: 'BRAK', subjectTypesList: [],
    });
    const [loading, setLoading] = useState<boolean>(false);
    useEffect(() => {
        if (isAuthenticated && role === 'user' && userId) {
            const parsedUserId = parseInt(userId, 10);
            if (!isNaN(parsedUserId)) {
                dispatch(getTeacher(parsedUserId))
                    .unwrap()
                    .catch((error: any) => {
                        enqueueSnackbar(`Błąd podczas pobierania danych nauczyciela: ${error.message}`, {variant: 'error'});
                    });
            } else {
                enqueueSnackbar('Nieprawidłowe ID użytkownika.', {variant: 'error'});
            }
        }
    }, [dispatch, isAuthenticated, role, userId, enqueueSnackbar]);

    useEffect(() => {
        if (teacher) {
            setFormData({
                id: teacher.id,
                firstName: teacher.firstName,
                lastName: teacher.lastName,
                degree: teacher.degree,
                subjectTypesList: teacher.subjectTypesList,
            });

            const slotsString = teacher.preferences.slots;
            if (slotsString) {
                try {
                    const slots = JSON.parse(slotsString);
                    const initialSlots: Record<number, SlotPreference> = {};
                    Object.keys(slots).forEach((key) => {
                        const slotId = Number(key);
                        const pref = slots[key];
                        if ([0, 1, -1].includes(pref)) {
                            initialSlots[slotId] = pref;
                        }
                    });
                    setPreferences(initialSlots);
                } catch (error) {
                    enqueueSnackbar('Błąd podczas parsowania preferencji slotów.', {variant: 'error'});
                }
            } else {
                setPreferences({});
            }
        }
    }, [teacher, enqueueSnackbar]);

    const handleSubmit = async () => {
        if (!formData.firstName.trim() || !formData.lastName.trim()) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }
        setLoading(true);

        const serializedPreferences: { [key: string]: string } = {
            slots: JSON.stringify(preferences),
        };
        //todo sprawdzic
        const teacherData: BackendTeacher = {
            id: formData.id,
            firstName: formData.firstName,
            lastName: formData.lastName,
            degree: formData.degree,
            preferences: serializedPreferences,
            subjectTypesList: formData.subjectTypesList
        };

        try {
            await dispatch(updateTeacher(teacherData)).unwrap();
            await dispatch(getTeacher(formData.id)).unwrap();
            enqueueSnackbar('Zaktualizowano!', {variant: 'success'});
        } catch (error: any) {
            enqueueSnackbar('Wystąpił błąd podczas zapisywania.', {variant: 'error'});
        } finally {
            setLoading(false);
        }
    };

    if (teacherLoading) {
        return (<div className="flex justify-center items-center h-full">
                <CircularProgress/>
            </div>);
    }

    if (teacherError) {
        enqueueSnackbar('Błąd podczas wczytywania profilu.', {variant: 'error'});
    }

    return (<div className="flex flex-col p-8">
            <div>
                <TeacherPreferences
                    preferences={preferences}
                    setPreferences={setPreferences}
                    loading={loading}
                />
            </div>
            <div className="pt-8">
                <ActionButton
                    onClick={handleSubmit}
                    disabled={loading}
                    tooltipText="Zaktualizuj"
                    icon={<SaveIcon/>}
                    colorScheme="primary"
                />
            </div>
        </div>);
};

export default UserDesiderata;
