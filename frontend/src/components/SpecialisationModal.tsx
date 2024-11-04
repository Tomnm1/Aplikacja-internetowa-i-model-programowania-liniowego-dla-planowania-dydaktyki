import React, { useEffect, useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Box,
    Fade,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { Specialisation, BackendSpecialisation, BackendFieldOfStudies, Cycle } from '../utils/Interfaces';
import { addSpecialisation, updateSpecialisation } from '../app/slices/specialisationSlice';
import { API_ENDPOINTS } from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';
import SaveButton from '../utils/SaveButton';
import { green } from "@mui/material/colors";
import CancelButton from "../utils/CancelButton";
import {GridRowId} from "@mui/x-data-grid";

interface SpecialisationModalProps {
    open: boolean;
    onClose: () => void;
    specialisation: Specialisation | null;
    isAdding: boolean;
}

const SpecialisationModal: React.FC<SpecialisationModalProps> = ({ open, onClose, specialisation, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [fieldOfStudies, setFieldOfStudies] = useState<BackendFieldOfStudies[]>([]);
    const [formData, setFormData] = useState<{
        id: GridRowId;
        name: string;
        cycle: Cycle;
        fieldOfStudyId: string;
        fieldOfStudyName: string;
    }>({
        id: specialisation?.id || '',
        name: specialisation?.name || '',
        cycle: specialisation?.cycle || Cycle.FIRST,
        fieldOfStudyId: specialisation?.fieldOfStudyId ? specialisation?.fieldOfStudyId.toString() : '',
        fieldOfStudyName: '',
    });

    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        fetch(API_ENDPOINTS.FIELD_OF_STUDIES)
            .then((res) => res.json())
            .then((data: BackendFieldOfStudies[]) => {
                setFieldOfStudies(data);
                if (isAdding && data.length > 0) {
                    setFormData((prev) => ({
                        ...prev,
                        fieldOfStudyId: data[0].fieldOfStudyId.toString(),
                        fieldOfStudyName: data[0].name,
                    }));
                }
            })
            .catch((err) => console.error('Failed to fetch fields of study', err));
    }, [isAdding]);

    useEffect(() => {
        if (specialisation && !isAdding) {
            setFormData({
                id: specialisation.id,
                name: specialisation.name,
                cycle: specialisation.cycle,
                fieldOfStudyId: specialisation?.fieldOfStudyId ? specialisation?.fieldOfStudyId.toString() : '',
                fieldOfStudyName: '',
            });
        } else if (isAdding) {
            setFormData((prev) => ({
                ...prev,
                fieldOfStudyName: fieldOfStudies.find(b => b.fieldOfStudyId.toString() === prev.fieldOfStudyId)?.name || '',
            }));
        } else {
            setFormData({
                id: '',
                name: '',
                cycle: Cycle.FIRST,
                fieldOfStudyId: '',
                fieldOfStudyName: "",
            });
        }
    }, [specialisation, isAdding, fieldOfStudies]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === 'cycle' ? value as Cycle : value,
        }));
    };

    const handleFieldOfStudyChange = (event: SelectChangeEvent) => {
        const selectedFieldId = event.target.value;
        const selectedField = fieldOfStudies.find(f => f.fieldOfStudyId.toString() === selectedFieldId);
        setFormData((prev) => ({
            ...prev,
            fieldOfStudyId: selectedFieldId,
            fieldOfStudyName: selectedField ? selectedField.name : "",
        }));
    };

    const handleCycleChange = (event: SelectChangeEvent<Cycle>) => {
        const selectedCycle = event.target.value as Cycle;
        setFormData((prev) => ({
            ...prev,
            cycle: selectedCycle,
        }));
    };

    const handleSubmit = async () => {
        if (!formData.name || !formData.cycle || !formData.fieldOfStudyId) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }

        setLoading(true);
        setSuccess(false);

        const specialisationData: BackendSpecialisation = {
            name: formData.name,
            cycle: formData.cycle,
            fieldOfStudy: {
                fieldOfStudyId: Number(formData.fieldOfStudyId),
                name: formData.fieldOfStudyName,
            },
        };

        if (!isAdding && formData.id !== '') {
            specialisationData.specialisationId = formData.id as number;
        }

        const MIN_SUCCESS_DURATION = 1000;
        const startTime = Date.now();

        try {
            if (isAdding) {
                await dispatch(addSpecialisation(specialisationData)).unwrap();
            } else {
                await dispatch(updateSpecialisation(specialisationData)).unwrap();
            }
            setSuccess(true);
            const elapsedTime = Date.now() - startTime;
            const remainingTime = MIN_SUCCESS_DURATION - elapsedTime;
            if (remainingTime > 0) {
                setTimeout(() => {
                    setLoading(false);
                    setSuccess(false);
                    onClose();
                }, remainingTime);
            } else {
                setLoading(false);
                setSuccess(false);
                onClose();
            }
        } catch (error) {
            alert('Wystąpił błąd podczas zapisywania specjalizacji.');
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box
                    sx={{
                        position: 'relative',
                        transition: 'background-color 0.5s ease',
                        backgroundColor: success ? 'white' : 'inherit',
                        minHeight: '200px',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center',
                        alignItems: 'center',
                        padding: success ? 4 : undefined,
                    }}
                >
                    {!success ? (
                        <>
                            <DialogTitle>{isAdding ? 'Dodaj Specjalizację' : 'Szczegóły Specjalizacji'}</DialogTitle>
                            <DialogContent>
                                <TextField
                                    margin="normal"
                                    label="Nazwa"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="cycle-label">Cykl</InputLabel>
                                    <Select
                                        labelId="cycle-label"
                                        value={formData.cycle}
                                        onChange={handleCycleChange}
                                        label="Cykl"
                                        variant="outlined"
                                    >
                                        <MenuItem value={Cycle.FIRST}>Pierwszy</MenuItem>
                                        <MenuItem value={Cycle.SECOND}>Drugi</MenuItem>
                                    </Select>
                                </FormControl>
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="fieldOfStudy-label">Kierunek</InputLabel>
                                    <Select
                                        labelId="fieldOfStudy-label"
                                        value={formData.fieldOfStudyId}
                                        onChange={handleFieldOfStudyChange}
                                        label="Kierunek"
                                        variant="outlined"
                                    >
                                        {fieldOfStudies.map((field) => (
                                            <MenuItem key={field.fieldOfStudyId} value={field.fieldOfStudyId}>
                                                {field.name}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </DialogContent>
                            <DialogActions>
                                <SaveButton onClick={handleSubmit} loading={loading} success={success} />
                                <CancelButton onClick={onClose} disabled={loading} />
                            </DialogActions>
                        </>
                    ) : (
                        <Fade in={success} timeout={1000}>
                            <Box
                                sx={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                }}
                            >
                                <CheckIcon sx={{ fontSize: 60, color: green[500], mb: 2 }} />
                                <Typography variant="h6" color="green">
                                    Zapisano!
                                </Typography>
                            </Box>
                        </Fade>
                    )}
                </Box>
            </Fade>
        </Dialog>
    );
};

export default SpecialisationModal;
