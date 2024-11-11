import React, { useEffect, useState } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
    Typography, Box, Fade,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { BackendSpecialisation, Semester} from '../utils/Interfaces';
import SaveButton from '../utils/SaveButton';
import { green } from "@mui/material/colors";
import CancelButton from "../utils/CancelButton";
import API_ENDPOINTS from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';
import {addSemester, fetchSemesters, updateSemester} from "../app/slices/semesterSlice.ts";

interface SemesterModalProps {
    open: boolean;
    onClose: () => void;
    semester: Semester | null;
    isAdding: boolean;
}

const SemesterModal: React.FC<SemesterModalProps> = ({ open, onClose, semester, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [specialisations, setSpecialisations] = useState<BackendSpecialisation[]>([]);
    const [formData, setFormData] = useState({
        id: semester?.id || '',
        number: semester?.number || '',
        specialisationId: semester?.specialisationId?.toString() || '',
    });
    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        if (isAdding) {
            fetch(API_ENDPOINTS.SPECIALISATIONS)
                .then(res => res.json())
                .then((data: BackendSpecialisation[]) => setSpecialisations(data))
                .catch(err => console.error('Failed to fetch specialisations', err));
        }
    }, [isAdding]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const value = e.target.value;
        setFormData((prev) => ({
            ...prev,
            number:value,
        }));
    };

    const handleSpecialisationChange = (event: SelectChangeEvent) => {
        const selectedSpecialisationId = event.target.value as string;
        //const selectedSpecialisation = specialisations.find(specialisation => specialisation!.specialisationId!.toString() === selectedSpecialisationId);
        setFormData({
            ...formData,
            specialisationId: selectedSpecialisationId,
        });
    };

    const handleSubmit = async () => {
        if (!formData.specialisationId) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }

        setLoading(true);

        const semesterData = {
            number: formData.number,
            specialisation: {
                specialisationId: Number(formData.specialisationId),
            },
            ...(isAdding ? {} : { semesterId: Number(formData.id) }),
        };

        try {
            const action = isAdding ? addSemester : updateSemester;
            await dispatch(action(semesterData)).unwrap();
            await dispatch(fetchSemesters());
            setSuccess(true);
            setTimeout(() => {
                setLoading(false);
                setSuccess(false);
                onClose();
            }, 1000);
        } catch (error) {
            console.log(error)
            alert('Wystąpił błąd podczas zapisywania semestru.');
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box sx={{ position: 'relative', minHeight: '200px', padding: 4 }}>
                    {!success ? (
                        <>
                            <DialogTitle>{isAdding ? 'Dodaj semestr' : 'Szczegóły semestru'}</DialogTitle>
                            <DialogContent>
                                {isAdding ? (
                                    <FormControl fullWidth margin="normal" disabled={loading}>
                                        <InputLabel id="semester-label">Specjalizacja</InputLabel>
                                        <Select
                                            labelId="slot-label"
                                            value={formData.specialisationId}
                                            onChange={handleSpecialisationChange}
                                            label="Specjalizacja"
                                            variant="outlined"
                                        >
                                            {specialisations.map((specialisation) => (
                                                <MenuItem key={specialisation.specialisationId} value={specialisation!.specialisationId!.toString()}>
                                                    {specialisation.name}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                ) : (
                                    <TextField
                                        margin="normal"
                                        label="Specjalizacja"
                                        value={formData.specialisationId}
                                        fullWidth
                                        disabled
                                    />
                                )}
                                <TextField
                                    margin="normal"
                                    label="Oznaczenie"
                                    name="number"
                                    value={formData.number}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                            </DialogContent>
                            <DialogActions>
                                <SaveButton onClick={handleSubmit} loading={loading} success={success} />
                                <CancelButton onClick={onClose} disabled={loading} />
                            </DialogActions>
                        </>
                    ) : (
                        <Fade in={success} timeout={1000}>
                            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                                <CheckIcon sx={{ fontSize: 60, color: green[500], mb: 2 }} />
                                <Typography variant="h6" color="green">
                                    Dodano!
                                </Typography>
                            </Box>
                        </Fade>
                    )}
                </Box>
            </Fade>
        </Dialog>
    );
};

export default SemesterModal;