import TeacherPreferences from "./TeacherPreferences.tsx";
import {BackendTeacher, SlotPreference, SubjectType} from "../utils/Interfaces.ts";
import {getTeacher, updateTeacher} from "../app/slices/teacherSlice.ts";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../app/store.ts";
import {useSnackbar} from "notistack";
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";

interface TeacherFormData {
    id: number;
    firstName: string;
    lastName: string;
    degree: string;
    subjectTypesList: SubjectType[] | [];
}

const UserDesiderata = () => {
    const dispatch = useDispatch<AppDispatch>();
    const [preferences, setPreferences] = useState<Record<number, SlotPreference>>({});
    const [loading, setLoading] = useState<boolean>(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        dispatch(getTeacher(21)).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania przedmiotów: ${error.message}`, {variant: 'error'});
        });
    }, []);

    const teacher = useSelector((state: RootState) => state.teachers.singleTeacher);
    const [formData] = useState<TeacherFormData>({
        id: teacher?.id ?? 0,
        firstName: teacher?.firstName ?? '',
        lastName: teacher?.lastName ?? '',
        degree: teacher?.degree ?? 'BRAK',
        subjectTypesList: teacher?.subjectTypesList ?? [],
    });

    useEffect(() => {
        const slotsString = teacher?.preferences.slots;
        if (typeof slotsString === undefined) return;
        const slots = JSON.parse(slotsString!);
        const initialSlots: Record<number, 0 | 1 | -1> = {};
        Object.keys(slots).forEach((key) => {
            initialSlots[Number(key)] = slots[key];
        })
        setPreferences({
            ...initialSlots,
        });

    }, []);

    const handleSubmit = async () => {
        if (!formData.firstName.trim() || !formData.lastName.trim()) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }
        setLoading(true);

        const serializedPreferences: { [key: string]: string } = {
            slots: JSON.stringify(preferences),
        };
        const teacherData: BackendTeacher = {
            id: formData.id,
            firstName: formData.firstName,
            lastName: formData.lastName,
            degree: formData.degree,
            preferences: serializedPreferences,
            subjectTypesList: formData.subjectTypesList,
        };

        try {
            await dispatch(updateTeacher(teacherData)).unwrap();
            await dispatch(getTeacher(21)).unwrap();
            enqueueSnackbar('Zaktualizowano!', {variant: 'success'});
            setLoading(false);
        } catch (error: any) {
            enqueueSnackbar('Wystąpił błąd podczas zapisywania.', {variant: 'error'});
            setLoading(false);
        }
    };

    return (<div className={"flex flex-col p-8"}>
            <div>
                <TeacherPreferences
                    preferences={preferences}
                    setPreferences={setPreferences}
                    loading={loading}
                />

            </div>
            <div className={"pt-8"}>
                <ActionButton
                    onClick={handleSubmit}
                    disabled={loading}
                    tooltipText={'Zaktualizuj'}
                    icon={<SaveIcon/>}
                    colorScheme={'primary'}
                />
            </div>
        </div>

    );
};

export default UserDesiderata;