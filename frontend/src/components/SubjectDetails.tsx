import React from 'react';
import {Box, Checkbox, FormControl, FormControlLabel, InputLabel, MenuItem, Select, TextField} from '@mui/material';
import {SelectChangeEvent} from '@mui/material/Select';
import {BackendSemester, cycleMapping, Language, languageMapping, Subject} from '../utils/Interfaces';

interface SubjectDetailsProps {
    formData: {
        SubjectId: number;
        name: string;
        language: Language;
        exam: boolean;
        mandatory: boolean;
        planned: boolean;
        semester: BackendSemester | { semesterId: string | number };
    };
    setFormData: React.Dispatch<React.SetStateAction<Subject>>;
    semesters: BackendSemester[];
    loading: boolean;
}

const SubjectDetails: React.FC<SubjectDetailsProps> = ({formData, setFormData, semesters, loading}) => {
    const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData((prev) => ({...prev, name: event.target.value}));
    };

    const handleLanguageChange = (event: SelectChangeEvent) => {
        setFormData((prev) => ({...prev, language: event.target.value as Language}));
    };

    const handleSemesterChange = (event: SelectChangeEvent) => {
        console.log(event.target.value);
        setFormData((prev) => ({
            ...prev, semester: {semesterId: event.target.value}
        }));
    };

    const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, checked} = event.target;
        setFormData((prev) => ({...prev, [name]: checked}));
    };

    return (<Box component="form" noValidate autoComplete="off">
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
                    variant="outlined"
                >
                    {Object.values(Language).map((language) => (<MenuItem key={language} value={language}>
                            {languageMapping[language]}
                        </MenuItem>))}
                </Select>
            </FormControl>
            <FormControl fullWidth margin="normal" disabled={loading} required>
                <InputLabel id="semester-label">Semestr</InputLabel>
                {/*TODO rozważyć/dostosować z Select na Autocomplete*/}
                <Select
                    labelId="semester-label"
                    value={formData.semester.semesterId!.toString()}
                    onChange={handleSemesterChange}
                    label="Semestr"
                    variant="outlined"
                >
                    {semesters.map((semester) => (
                        <MenuItem key={semester.semesterId} value={semester.semesterId?.toString()}>
                            {`${semester.number} - (${semester.specialisation.name} - ${semester.specialisation.fieldOfStudy?.name} - ${cycleMapping[semester.specialisation.cycle]})`}
                        </MenuItem>))}
                </Select>
            </FormControl>
            <Box display="flex" justifyContent="space-around" mt={2}>
                <FormControlLabel
                    control={<Checkbox
                        checked={formData.exam}
                        onChange={handleCheckboxChange}
                        name="exam"
                        color="primary"
                        disabled={loading}
                    />}
                    label="Egzamin"
                />
                <FormControlLabel
                    control={<Checkbox
                        checked={formData.mandatory}
                        onChange={handleCheckboxChange}
                        name="mandatory"
                        color="primary"
                        disabled={loading}
                    />}
                    label="Obowiązkowy"
                />
                <FormControlLabel
                    control={<Checkbox
                        checked={formData.planned}
                        onChange={handleCheckboxChange}
                        name="planned"
                        color="primary"
                        disabled={loading}
                    />}
                    label="Planowany"
                />
            </Box>
        </Box>);
};

export default SubjectDetails;
