import React, {useEffect, useState} from 'react';
import {
    Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Fade, Step, StepLabel, Stepper
} from '@mui/material';
import {useDispatch} from 'react-redux';
import {AppDispatch, store} from '../app/store';
import {BackendSemester, BackendSubject, BackendSubjectType, Language, Subject} from '../utils/Interfaces';
import ActionButton from "../utils/ActionButton.tsx";
import {useSnackbar} from 'notistack';
import SubjectDetails from './SubjectDetails';
import SubjectTypesForm from './SubjectTypesForm';
import {addSubject, fetchSubject, updateSubject} from "../app/slices/subjectSlice";
import API_ENDPOINTS from '../app/urls';
import {addSubjectType, fetchSubjectType, updateSubjectType} from "../app/slices/subjectTypeSlice.ts";
import ClearIcon from '@mui/icons-material/Clear';
import SaveIcon from '@mui/icons-material/Save';
import NavigateNextIcon from "@mui/icons-material/NavigateNext";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {fetchWithAuth} from "../app/fetchWithAuth.ts";


interface SubjectModalProps {
    open: boolean;
    onClose: () => void;
    subject: Subject | null;
    isAdding: boolean;
}

const steps = ['Szczegóły przedmiotu', 'Typy przedmiotu'];

const SubjectModal: React.FC<SubjectModalProps> = ({open, onClose, subject, isAdding}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();
    const [activeStep, setActiveStep] = useState(0);
    const [semesters, setSemesters] = useState<BackendSemester[]>([]);
    const [formData, setFormData] = useState<Subject>({
        SubjectId: subject?.SubjectId || 0,
        name: subject?.name || '',
        language: subject?.language || Language.POLSKI,
        exam: subject?.exam || false,
        mandatory: subject?.mandatory || false,
        planned: subject?.planned || false,
        semester: {semesterId: subject?.semester?.semesterId?.toString() || ''},
    });
    const [subjectTypes, setSubjectTypes] = useState<BackendSubjectType[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        if (open) {
            fetchWithAuth(API_ENDPOINTS.SEMESTERS)
                .then(res => res.json())
                .then((data: BackendSemester[]) => setSemesters(data))
                .catch(err => {
                    enqueueSnackbar(`Wystąpił błąd przy pobieraniu semestrów: ${err}`, {variant: 'error'});
                });

            if (!isAdding && subject) {
                dispatch(fetchSubjectType()).unwrap()
                    .then(() => {
                        const types = store.getState().subjectsTypes.rows.filter(st => st.subject.SubjectId === subject.SubjectId);
                        setSubjectTypes(types.map(st => st as BackendSubjectType));
                    })
                    .catch(err => {
                        enqueueSnackbar(`Wystąpił błąd przy pobieraniu typów przedmiotu: ${err}`, {variant: 'error'});
                    });
            } else {
                setSubjectTypes([]);
            }
        }
    }, [open, enqueueSnackbar, isAdding, subject, dispatch]);

    const handleNext = () => {
        if (!formData.name || !formData.language || !formData.semester.semesterId) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }
        setActiveStep(prev => prev + 1);
    };

    const handleBack = () => {
        setActiveStep(prev => prev - 1);
    };

    const handleSubmit = async () => {
        if (!formData.name || !formData.language || !formData.semester.semesterId) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }
        setLoading(true);

        const subjectData: BackendSubject = {
            ...(isAdding ? {} : {SubjectId: Number(formData.SubjectId)}),
            name: formData.name,
            language: formData.language,
            exam: formData.exam,
            mandatory: formData.mandatory,
            planned: formData.planned,
            semester: {
                semesterId: Number(formData.semester.semesterId),
            },
        };

        try {
            const action = isAdding ? addSubject : updateSubject;
            const savedSubject = await dispatch(action(subjectData)).unwrap();

            const subjectId = savedSubject.SubjectId;
            if (!subjectId) {
                enqueueSnackbar('SubjectId jest nieznane', {variant: 'error'});
            }
            for (const subjectType of subjectTypes) {
                const subjectTypeData: BackendSubjectType = {
                    ...subjectType, subject: {SubjectId: subjectId},
                };
                console.log(subjectTypeData);
                if (subjectType.subjectTypeId) {
                    await dispatch(updateSubjectType(subjectTypeData)).unwrap();
                } else {
                    await dispatch(addSubjectType(subjectTypeData)).unwrap();
                }
            }
            enqueueSnackbar(isAdding ? 'Dodano!' : 'Zaktualizowano!', {variant: 'success'});
            await dispatch(fetchSubject());
            setLoading(false);
            onClose();

        } catch (error) {
            enqueueSnackbar(`Wystąpił błąd przy ${isAdding ? 'dodawaniu' : 'aktualizacji'} rekordu: ${error.message || error}`, {variant: 'error'});
            setLoading(false);
        }
    };

    const renderStepContent = (step: number) => {
        switch (step) {
            case 0:
                return (<SubjectDetails
                        formData={formData}
                        setFormData={setFormData}
                        semesters={semesters}
                        loading={loading}
                    />);
            case 1:
                return (<SubjectTypesForm
                        subjectTypes={subjectTypes}
                        setSubjectTypes={setSubjectTypes}
                        semester={formData.semester.semesterId as number}
                        loading={loading}
                    />);
            default:
                return null;
        }
    };

    return (<Dialog open={open} onClose={onClose} fullWidth maxWidth="md">
            <Fade in={open} timeout={500}>
                <Box sx={{position: 'relative', minHeight: '400px', padding: 4}}>
                    <DialogTitle>{isAdding ? 'Dodaj przedmiot' : 'Edytuj przedmiot'}</DialogTitle>
                    <Stepper activeStep={activeStep} alternativeLabel>
                        {steps.map((label) => (<Step key={label}>
                                <StepLabel>{label}</StepLabel>
                            </Step>))}
                    </Stepper>
                    <DialogContent>
                        {renderStepContent(activeStep)}
                    </DialogContent>
                    <DialogActions className="flex justify-between">
                        <Box>
                            {activeStep > 0 && (<Button onClick={handleBack} disabled={loading}>
                                    <ArrowBackIcon/>
                                    Powrót
                                </Button>)}
                        </Box>
                        <Box>
                            {activeStep < steps.length - 1 && (<ActionButton onClick={handleNext} disabled={loading}
                                                                             tooltipText={'Przejdź do następnego kroku'}
                                                                             icon={<NavigateNextIcon/>}
                                                                             colorScheme={'primary'}/>)}
                            {activeStep === steps.length - 1 && (<div className={"flex"}>
                                    <ActionButton onClick={handleSubmit} disabled={loading}
                                                  tooltipText={isAdding ? 'Dodaj' : 'Zaktualizuj'} icon={<SaveIcon/>}
                                                  colorScheme={'primary'}/>
                                    <ActionButton onClick={onClose} disabled={loading} tooltipText={"Anuluj"}
                                                  icon={<ClearIcon/>} colorScheme={'secondary'}/>
                                </div>)}
                        </Box>
                    </DialogActions>
                </Box>
            </Fade>
        </Dialog>);
};

export default SubjectModal;
