import React, { useEffect, useState } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
    Typography, Box, Fade, Checkbox, ListItemText, OutlinedInput,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { Teacher, degrees, SubjectType } from '../utils/Interfaces';
import SaveButton from '../utils/SaveButton';
import { green } from "@mui/material/colors";
import CancelButton from "../utils/CancelButton";
import { addTeacher, updateTeacher, fetchTeachers } from '../app/slices/teacherSlice';
import { API_ENDPOINTS } from '../app/urls';

interface TeacherModalProps {
    open: boolean;
    onClose: () => void;
    teacher: Teacher | null;
    isAdding: boolean;
}

const TeacherModal: React.FC<TeacherModalProps> = ({ open, onClose, teacher, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [subjectTypes, setSubjectTypes] = useState<SubjectType[]>([]);
    const [formData, setFormData] = useState({
        id: teacher?.id || '',
        firstName: teacher?.firstName || '',
        lastName: teacher?.lastName || '',
        degree: teacher?.degree || 'BRAK',
        preferences: JSON.stringify(teacher?.preferences || {}),
        subjectTypesList: teacher?.subjectTypesList || [],
    });
    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        fetch(API_ENDPOINTS.SUBJECT_TYPE)
            .then((res) => res.json())
            .then((data: SubjectType[]) => setSubjectTypes(data))
            .catch((err) => console.error('Failed to fetch subject types', err));
    }, []);

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = event.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubjectTypesChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        const { value } = event.target;
        setFormData((prev) => ({
            ...prev,
            subjectTypesList: value as number[],
        }));
    };

    const handleSubmit = async () => {
        if (!formData.firstName || !formData.lastName) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }

        let preferencesObj = {};
        try {
            preferencesObj = JSON.parse(formData.preferences);
        } catch (error) {
            alert('Preferences must be a valid JSON object');
            return;
        }

        setLoading(true);

        const teacherData: Teacher = {
            id: isAdding ? 0 : formData.id,
            firstName: formData.firstName,
            lastName: formData.lastName,
            degree: formData.degree,
            preferences: preferencesObj,
            subjectTypesList: formData.subjectTypesList,
        };

        try {
            const action = isAdding ? addTeacher : updateTeacher;
            await dispatch(action(teacherData)).unwrap();
            await dispatch(fetchTeachers());
            setSuccess(true);
            setTimeout(() => {
                setLoading(false);
                setSuccess(false);
                onClose();
            }, 1000);
        } catch (error) {
            console.error(error);
            alert('Wystąpił błąd podczas zapisywania nauczyciela.');
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box sx={{ position: 'relative', minHeight: '200px', padding: 4 }}>
                    {!success ? (
                        <>
                            <DialogTitle>{isAdding ? 'Dodaj nauczyciela' : 'Szczegóły nauczyciela'}</DialogTitle>
                            <DialogContent>
                                <TextField
                                    margin="normal"
                                    label="Imię"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <TextField
                                    margin="normal"
                                    label="Nazwisko"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="degree-label">Stopień</InputLabel>
                                    <Select
                                        labelId="degree-label"
                                        name="degree"
                                        value={formData.degree}
                                        onChange={(e) =>
                                            setFormData({ ...formData, degree: e.target.value as string })
                                        }
                                        label="Stopień"
                                        variant="outlined"
                                    >
                                        {Object.entries(degrees).map(([key, displayName]) => (
                                            <MenuItem key={key} value={key}>
                                                {displayName}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                                <TextField
                                    margin="normal"
                                    label="Preferencje"
                                    name="preferences"
                                    value={formData.preferences}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="subject-types-label">Typy Przedmiotów</InputLabel>
                                    <Select
                                        labelId="subject-types-label"
                                        multiple
                                        value={formData.subjectTypesList}
                                        onChange={handleSubjectTypesChange}
                                        input={<OutlinedInput label="Typy Przedmiotów" />}
                                        renderValue={(selected) =>
                                            subjectTypes
                                                .filter((type) => (selected as number[]).includes(type.id))
                                                .map((type) => type.name)
                                                .join(', ')
                                        }
                                        variant="outlined"
                                    >
                                        {subjectTypes.map((type) => (
                                            <MenuItem key={type.id} value={type.id}>
                                                <Checkbox
                                                    checked={formData.subjectTypesList.includes(type.id)}
                                                />
                                                <ListItemText primary={type.name} />
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
                            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                                <CheckIcon sx={{ fontSize: 60, color: green[500], mb: 2 }} />
                                <Typography variant="h6" color="green">
                                    {isAdding ? 'Dodano!' : 'Zapisano!'}
                                </Typography>
                            </Box>
                        </Fade>
                    )}
                </Box>
            </Fade>
        </Dialog>
    );
};

export default TeacherModal;
