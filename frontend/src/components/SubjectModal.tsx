import React, { useEffect, useState } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
    Typography, Box, Fade, Checkbox, FormControlLabel
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { Subject, BackendSubject, Language, languageMapping, BackendSemester } from '../utils/Interfaces';
import SaveButton from '../utils/SaveButton';
import { green } from "@mui/material/colors";
import CancelButton from "../utils/CancelButton";
import { fetchSubject, addSubject, updateSubject } from "../app/slices/subjectSlice";
import API_ENDPOINTS from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';

interface SubjectModalProps {
    open: boolean;
    onClose: () => void;
    subject: Subject | null;
    isAdding: boolean;
}

const SubjectModal: React.FC<SubjectModalProps> = ({ open, onClose, subject, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [semesters, setSemesters] = useState<BackendSemester[]>([]);
    const [formData, setFormData] = useState({
        id: subject?.SubjectId || '',
        name: subject?.name || '',
        language: subject?.language || Language.POLSKI,
        exam: subject?.exam || false,
        mandatory: subject?.mandatory || false,
        planned: subject?.planned || false,
        semesterId: subject?.semester?.semesterId?.toString() || '',
    });
    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        fetch(API_ENDPOINTS.SEMESTERS)
            .then(res => res.json())
            .then((data: BackendSemester[]) => setSemesters(data))
            .catch(err => console.error('Failed to fetch semesters', err));
    }, []);

    const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, name: event.target.value });
    };

    const handleLanguageChange = (event: SelectChangeEvent) => {
        setFormData({ ...formData, language: event.target.value as Language });
    };

    const handleSemesterChange = (event: SelectChangeEvent) => {
        setFormData({ ...formData, semesterId: event.target.value });
    };

    const handleExamChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, exam: event.target.checked });
    };

    const handleMandatoryChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, mandatory: event.target.checked });
    };

    const handlePlannedChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, planned: event.target.checked });
    };

    const handleSubmit = async () => {
        if (!formData.name || !formData.language || !formData.semesterId) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }
        setLoading(true);

        const subjectData: BackendSubject = {
            ...(isAdding ? {} : { SubjectId: Number(formData.id) }),
            name: formData.name,
            language: formData.language,
            exam: formData.exam,
            mandatory: formData.mandatory,
            planned: formData.planned,
            semester: {
                semesterId: Number(formData.semesterId),
            },
        };
        console.log(subjectData);
        try {
            const action = isAdding ? addSubject : updateSubject;
            await dispatch(action(subjectData)).unwrap();
            await dispatch(fetchSubject());
            setSuccess(true);
            setTimeout(() => {
                setLoading(false);
                setSuccess(false);
                onClose();
            }, 1000);
        } catch (error) {
            console.error(error);
            alert('Wystąpił błąd podczas zapisywania przedmiotu.');
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box sx={{ position: 'relative', minHeight: '200px', padding: 4 }}>
                    {!success ? (
                        <>
                            <DialogTitle>{isAdding ? 'Dodaj przedmiot' : 'Szczegóły przedmiotu'}</DialogTitle>
                            <DialogContent>
                                <TextField
                                    margin="normal"
                                    label="Nazwa"
                                    value={formData.name}
                                    onChange={handleNameChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="language-label">Język</InputLabel>
                                    <Select
                                        labelId="language-label"
                                        value={formData.language}
                                        onChange={handleLanguageChange}
                                        label="Język"
                                        variant="outlined"
                                    >
                                        {Object.values(Language).map((language) => (
                                            <MenuItem key={language} value={language}>
                                                {languageMapping[language]}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="semester-label">Semestr</InputLabel>
                                    <Select
                                        labelId="semester-label"
                                        value={formData.semesterId}
                                        onChange={handleSemesterChange}
                                        label="Semestr"
                                        variant="outlined"
                                    >
                                        {semesters.map((semester) => (
                                            <MenuItem key={semester.semesterId} value={semester.semesterId?.toString()}>
                                                {semester.number}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                                <FormControl margin="normal" disabled={loading}>
                                    <FormControlLabel
                                        control={
                                            <Checkbox
                                                checked={formData.exam}
                                                onChange={handleExamChange}
                                                name="exam"
                                                color="primary"
                                            />
                                        }
                                        label="Egzamin"
                                    />
                                </FormControl>
                                <FormControl margin="normal" disabled={loading}>
                                    <FormControlLabel
                                        control={
                                            <Checkbox
                                                checked={formData.mandatory}
                                                onChange={handleMandatoryChange}
                                                name="mandatory"
                                                color="primary"
                                            />
                                        }
                                        label="Obowiązkowy"
                                    />
                                </FormControl>
                                <FormControl margin="normal" disabled={loading}>
                                    <FormControlLabel
                                        control={
                                            <Checkbox
                                                checked={formData.planned}
                                                onChange={handlePlannedChange}
                                                name="planned"
                                                color="primary"
                                            />
                                        }
                                        label="Planowany"
                                    />
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
                                    {isAdding ? 'Dodano!' : 'Zaktualizowano!'}
                                </Typography>
                            </Box>
                        </Fade>
                    )}
                </Box>
            </Fade>
        </Dialog>
    );
};

export default SubjectModal;
