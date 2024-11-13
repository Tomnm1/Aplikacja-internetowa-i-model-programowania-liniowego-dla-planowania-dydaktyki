import React from 'react';
import {
    TextField, FormControl, InputLabel, Select, MenuItem,
    Box, Checkbox, FormControlLabel
} from '@mui/material';
import { SelectChangeEvent } from '@mui/material/Select';
import {Language, BackendSemester, languageMapping} from '../utils/Interfaces';

interface SubjectDetailsProps {
    formData: {
        //TODO do przemyślenia czy na pewno string | number;
        id: string | number;
        name: string;
        language: Language;
        exam: boolean;
        mandatory: boolean;
        planned: boolean;
        semesterId: string;
    };
    setFormData: React.Dispatch<React.SetStateAction<any>>;
    semesters: BackendSemester[];
    loading: boolean;
}

const SubjectDetails: React.FC<SubjectDetailsProps> = ({ formData, setFormData, semesters, loading }) => {
    //TODO: poprawić 4x typ any
    const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData((prev: any) => ({ ...prev, name: event.target.value }));
    };

    const handleLanguageChange = (event: SelectChangeEvent) => {
        setFormData((prev: any) => ({ ...prev, language: event.target.value as Language }));
    };

    const handleSemesterChange = (event: SelectChangeEvent) => {
        setFormData((prev: any) => ({ ...prev, semesterId: event.target.value }));
    };

    const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, checked } = event.target;
        setFormData((prev: any) => ({ ...prev, [name]: checked }));
    };

    return (
        <Box component="form" noValidate autoComplete="off">
            <TextField
                margin="normal"
                label="Nazwa"
                value={formData.name}
                onChange={handleNameChange}
                fullWidth
                disabled={loading}
                required
            />
            <FormControl fullWidth margin="normal" disabled={loading} required>
                <InputLabel id="language-label">Język</InputLabel>
                <Select
                    labelId="language-label"
                    value={formData.language}
                    onChange={handleLanguageChange}
                    label="Język"
                >
                    {Object.values(Language).map((language) => (
                        <MenuItem key={language} value={language}>
                            {languageMapping[language]}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <FormControl fullWidth margin="normal" disabled={loading} required>
                <InputLabel id="semester-label">Semestr</InputLabel>
                {/*TODO rozważyć/dostosować z Select na Autocomplete*/}
                <Select
                    labelId="semester-label"
                    value={formData.semesterId}
                    onChange={handleSemesterChange}
                    label="Semestr"
                >
                    {semesters.map((semester) => (
                        <MenuItem key={semester.semesterId} value={semester.semesterId?.toString()}>
                            {/*TODO dodać mapowanie semester.specialisation.cycle*/}
                            {`${semester.number} - (${semester.specialisation.name} - ${semester.specialisation.fieldOfStudy?.name} - ${semester.specialisation.cycle})`}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <Box display="flex" justifyContent="space-around" mt={2}>
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={formData.exam}
                            onChange={handleCheckboxChange}
                            name="exam"
                            color="primary"
                            disabled={loading}
                        />
                    }
                    label="Egzamin"
                />
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={formData.mandatory}
                            onChange={handleCheckboxChange}
                            name="mandatory"
                            color="primary"
                            disabled={loading}
                        />
                    }
                    label="Obowiązkowy"
                />
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={formData.planned}
                            onChange={handleCheckboxChange}
                            name="planned"
                            color="primary"
                            disabled={loading}
                        />
                    }
                    label="Planowany"
                />
            </Box>
        </Box>
    );
};

export default SubjectDetails;
