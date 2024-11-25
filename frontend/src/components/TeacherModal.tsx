import React, {useEffect, useState} from 'react';
import {
    Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Fade, Step, StepLabel, Stepper
} from '@mui/material';
import {useDispatch, useSelector} from 'react-redux';
import {AppDispatch, RootState} from '../app/store';
import {Teacher} from '../utils/Interfaces';
import {addTeacher, fetchTeachers, updateTeacher} from '../app/slices/teacherSlice';
import {fetchSlots} from '../app/slices/slotsSlice';
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

type SlotPreference = 'neutral' | 'green' | 'red';

const TeacherModal: React.FC<TeacherModalProps> = ({open, onClose, teacher, isAdding}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();
    const [activeStep, setActiveStep] = useState(0);

    const [formData, setFormData] = useState({
        id: teacher?.id || '',
        firstName: teacher?.firstName || '',
        lastName: teacher?.lastName || '',
        degree: teacher?.degree || 'BRAK',
        subjectTypesList: teacher?.subjectTypesList || [],
    });

    const [preferences, setPreferences] = useState<Record<string, SlotPreference>>({});

    const [loading, setLoading] = useState<boolean>(false);

    const slots = useSelector((state: RootState) => state.slots.rows);
    const slotsLoading = useSelector((state: RootState) => state.slots.loading);

    useEffect(() => {
        if (open) {
            if (slots.length === 0 && !slotsLoading) {
                dispatch(fetchSlots()).unwrap().catch((error: any) => {
                    enqueueSnackbar(`Błąd podczas pobierania slotów: ${error.message}`, {variant: 'error'});
                });
            }

            if (teacher?.preferences) {
                setPreferences(teacher.preferences as Record<string, SlotPreference>);
            } else {
                const initialPreferences: Record<string, SlotPreference> = {};
                slots.forEach((slot) => {
                    initialPreferences[slot.slot_id.toString()] = 'neutral';
                });
                setPreferences(initialPreferences);
            }
            setActiveStep(0);
        }
    }, [open, teacher, dispatch, slots, slotsLoading, enqueueSnackbar]);

    const handleNext = () => {
        if (activeStep === 0) {
            if (!formData.firstName || !formData.lastName) {
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
        if (!formData.firstName || !formData.lastName) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }
        setLoading(true);

        const teacherData: Teacher = {
            id: isAdding ? 0 : formData.id,
            firstName: formData.firstName,
            lastName: formData.lastName,
            degree: formData.degree,
            preferences: preferences,
            subjectTypesList: formData.subjectTypesList,
        };

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
                    <DialogTitle>{isAdding ? 'Dodaj nauczyciela' : `Edytuj nauczyciela ${teacher!.firstName} ${teacher!.lastName}`}</DialogTitle>
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
                                slots={slots}
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
                                {activeStep === steps.length - 1 && (<div className={"flex"}>
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
