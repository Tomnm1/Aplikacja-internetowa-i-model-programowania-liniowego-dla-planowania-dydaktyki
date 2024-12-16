import React, {useEffect, useState} from 'react';
import {
    Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Fade, Step, StepLabel, Stepper
} from '@mui/material';
import {useDispatch} from 'react-redux';
import {AppDispatch} from '../app/store';
import {BackendTeacher, SlotPreference, SubjectType, Teacher} from '../utils/Interfaces';
import {addTeacher, fetchTeachers, updateTeacher} from '../app/slices/teacherSlice';
import {useSnackbar} from 'notistack';
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";
import NavigateNextIcon from "@mui/icons-material/NavigateNext";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import TeacherDetails from './TeacherDetails';
import TeacherPreferences from './TeacherPreferences';

interface TeacherModalProps {
    open: boolean;
    onClose: () => void;
    teacher: Teacher | null;
    isAdding: boolean;
}

const steps = ['Dane nauczyciela', 'Preferencje godzin'];

interface TeacherFormData {
    id: number;
    firstName: string;
    secondName: string;
    lastName: string;
    degree: string;
    isAdmin: boolean;
    subjectTypesList: SubjectType[] | [];
    innerId: number;
    usosId: number;
}

const TeacherModal: React.FC<TeacherModalProps> = ({open, onClose, teacher, isAdding}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();
    const [activeStep, setActiveStep] = useState(0);

    const [formData, setFormData] = useState<TeacherFormData>({
        id: teacher?.id ?? 0,
        firstName: teacher?.firstName ?? '',
        secondName: teacher?.secondName ?? '',
        lastName: teacher?.lastName ?? '',
        degree: teacher?.degree ?? 'BRAK',
        isAdmin: teacher?.isAdmin ?? false,
        subjectTypesList: teacher?.subjectTypesList ?? [],
        innerId: teacher?.innerId ?? 0,
        usosId: teacher?.usosId ?? 0,
    });

    const [preferences, setPreferences] = useState<Record<number, SlotPreference>>({});
    const [loading, setLoading] = useState<boolean>(false);
    useEffect(() => {
        if (open) {
            const slotsString = teacher?.preferences.slots;
            if (slotsString){
                const slots = JSON.parse(slotsString!);
                const initialSlots: Record<number, 0 | 1 | -1> = {};
                Object.keys(slots).forEach((key) => {
                    initialSlots[Number(key)] = slots[key];
                })
                setPreferences({
                    ...initialSlots,
                });
            }
            setActiveStep(0);
        }
    }, []);
    const handleNext = () => {
        if (activeStep === 0) {
            if (!formData.firstName.trim() || !formData.lastName.trim()) {
                enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
                return;
            }
        }
        setActiveStep((prev) => prev + 1);
    };

    const handleBack = () => {
        setActiveStep((prev) => prev - 1);
    };

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
            id: isAdding ? 0 : formData.id,
            firstName: formData.firstName,
            secondName: formData.secondName,
            lastName: formData.lastName,
            degree: formData.degree,
            isAdmin: formData.isAdmin,
            preferences: serializedPreferences,
            subjectTypesList: formData.subjectTypesList,
            innerId: formData.innerId,
            usosId: formData.usosId,
        };
        console.log(teacherData);

        try {
            const action = isAdding ? addTeacher : updateTeacher;
            await dispatch(action(teacherData)).unwrap();
            await dispatch(fetchTeachers()).unwrap();
            enqueueSnackbar(isAdding ? 'Dodano!' : 'Zaktualizowano!', {variant: 'success'});
            setLoading(false);
            onClose();
        } catch (error: any) {
            enqueueSnackbar('Wystąpił błąd podczas zapisywania nauczyciela.', {variant: 'error'});
            setLoading(false);
        }
    };

    return (<Dialog open={open} onClose={onClose} fullWidth maxWidth="lg">
        <Fade in={open} timeout={500}>
            <Box sx={{position: 'relative', minHeight: '400px', padding: 4}}>
                <DialogTitle>
                    {isAdding ? 'Dodaj nauczyciela' : teacher ? `Edytuj nauczyciela ${teacher.firstName} ${teacher.lastName}` : 'Edytuj nauczyciela'}
                </DialogTitle>
                <Stepper activeStep={activeStep} alternativeLabel sx={{marginBottom: 2}}>
                    {steps.map((label) => (<Step key={label}>
                        <StepLabel>{label}</StepLabel>
                    </Step>))}
                </Stepper>
                <DialogContent>
                    {activeStep === 0 && (<TeacherDetails
                        formData={formData}
                        setFormData={setFormData}
                        loading={loading}
                    />)}
                    {activeStep === 1 && (<TeacherPreferences
                        preferences={preferences}
                        setPreferences={setPreferences}
                        loading={loading}
                    />)}
                </DialogContent>
                <DialogActions>
                    <Box display="flex" justifyContent="space-between" width="100%">
                        <Box>
                            {activeStep > 0 && (<Button onClick={handleBack} disabled={loading}>
                                <ArrowBackIcon/>
                                Powrót
                            </Button>)}
                        </Box>
                        <Box>
                            {activeStep < steps.length - 1 && (<ActionButton
                                onClick={handleNext}
                                disabled={loading}
                                tooltipText={'Następny krok'}
                                icon={<NavigateNextIcon/>}
                                colorScheme={'primary'}
                            />)}
                            {activeStep === steps.length - 1 && (<div className="flex">
                                <ActionButton
                                    onClick={handleSubmit}
                                    disabled={loading}
                                    tooltipText={isAdding ? 'Dodaj' : 'Zaktualizuj'}
                                    icon={<SaveIcon/>}
                                    colorScheme={'primary'}
                                />
                                <ActionButton
                                    onClick={onClose}
                                    disabled={loading}
                                    tooltipText={"Anuluj"}
                                    icon={<ClearIcon/>}
                                    colorScheme={'secondary'}
                                />
                            </div>)}
                        </Box>
                    </Box>
                </DialogActions>
            </Box>
        </Fade>
    </Dialog>);
};

export default TeacherModal;
