import React, {useEffect} from 'react';
import {Box, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, TextField,} from '@mui/material';
import {degrees, SubjectType} from '../utils/Interfaces';
import {useDispatch, useSelector} from 'react-redux';
import {fetchSubjectType} from '../app/slices/subjectTypeSlice';
import {AppDispatch, RootState} from '../app/store';
import {useSnackbar} from 'notistack';

interface TeacherDetailsProps {
    formData: {
        id: number;
        firstName: string;
        secondName: string;
        lastName: string;
        degree: string;
        subjectTypesList: SubjectType[] | [];
        innerId: number;
        usosId: number;
    };
    setFormData: React.Dispatch<React.SetStateAction<any>>;
    loading: boolean;
}

const TeacherDetails: React.FC<TeacherDetailsProps> = ({formData, setFormData, loading}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();

    const subjectTypes: SubjectType[] = useSelector((state: RootState) => state.subjectsTypes.rows);
    const subjectTypesLoading = useSelector((state: RootState) => state.subjectsTypes.loading);

    useEffect(() => {
        if (subjectTypes.length === 0 && !subjectTypesLoading) {
            dispatch(fetchSubjectType()).unwrap().catch((error: any) => {
                enqueueSnackbar(`Błąd podczas pobierania typów przedmiotów: ${error.message}`, {variant: 'error'});
            });
        }
    }, [dispatch, subjectTypes.length, subjectTypesLoading, enqueueSnackbar]);

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {name, value} = event.target;
        setFormData((prev: any) => ({
            ...prev, [name]: value,
        }));
    };

    const handleDegreeChange = (event: SelectChangeEvent) => {
        const {name, value} = event.target;
        setFormData((prev: any) => ({
            ...prev, [name as string]: value as string,
        }));
    };

    return (<Box component="form" noValidate autoComplete="off">
        <TextField
            margin="normal"
            label="Imię"
            name="firstName"
            value={formData.firstName}
            onChange={handleInputChange}
            fullWidth
            disabled={loading}
            required
        />
        <TextField
            margin="normal"
            label="Drugie imię"
            name="secondName"
            value={formData.secondName}
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
            required
        />
        <FormControl fullWidth margin="normal" disabled={loading} required>
            <InputLabel id="degree-label">Stopień</InputLabel>
            <Select
                labelId="degree-label"
                name="degree"
                value={formData.degree}
                onChange={handleDegreeChange}
                label="Stopień"
                variant="outlined"
            >
                {Object.entries(degrees).map(([key, displayName]) => (<MenuItem key={key} value={key}>
                    {displayName}
                </MenuItem>))}
            </Select>
        </FormControl>
        <TextField
            margin="normal"
            label="Wewnętrzne ID"
            name="innerId"
            value={formData.innerId}
            onChange={handleInputChange}
            fullWidth
            disabled={loading}
            required
        />
        <TextField
            margin="normal"
            label="USOS ID"
            name="usosId"
            value={formData.usosId}
            onChange={handleInputChange}
            fullWidth
            disabled={loading}
            required
        />
    </Box>);
};

export default TeacherDetails;
