import React, {useEffect, useState} from 'react';
import {
    Autocomplete,
    Dialog, DialogActions, DialogContent, DialogTitle, TextField,
} from '@mui/material';
import {BackendSubjectType, BackendTeacher, teacherListDTO} from '../utils/Interfaces';
import {useSnackbar} from 'notistack';
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";
import API_ENDPOINTS from "../app/urls.ts";

interface SubjectTypesTeachersModalProps {
    open: boolean;
    onClose: () => void;
    typeData: teacherListDTO | null;
    onSave: (type: teacherListDTO) => void;
    subjectType: BackendSubjectType | null;
}

const SubjectTypesTeachersModal: React.FC<SubjectTypesTeachersModalProps> = ({
                                                                                 open,
                                                                                 onClose,
                                                                                 typeData,
                                                                                 onSave,
                                                                                 subjectType
                                                                             }) => {
    const {enqueueSnackbar} = useSnackbar();
    const [teachers, setTeachers] = useState<BackendTeacher[]>([]);
    const [formData, setFormData] = useState<teacherListDTO>({
        id: typeData?.id || 0,
        teacherId: typeData?.teacherId || 0,
        numHours: typeData?.numHours || 0,
        subjectTypeId: typeData?.subjectTypeId || subjectType?.subjectTypeId || 0,
        teacherFirstName: typeData?.teacherFirstName || '',
        teacherLastName: typeData?.teacherLastName || '',
        frontId: typeData?.frontId || subjectType?.frontId || '',
    });

    useEffect(() => {
        if (!typeData) {
            const usedTeacher = subjectType?.teachersList.map(t => t.teacherId) || [];
            fetch(API_ENDPOINTS.TEACHERS)
                .then(res => res.json())
                .then((data: BackendTeacher[]) => setTeachers(data.filter(t => !usedTeacher.includes(t.id!))))
                .catch(err => {
                    enqueueSnackbar(`Wystąpił błąd przy pobieraniu prowadzących: ${err}`, {variant: 'error'});
                });
        }
    }, [enqueueSnackbar, typeData]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setFormData((prev) => ({
            ...prev, numHours: Number(value),
        }));
    };

    const handleTeacherChange = (selectedTeacherId: number) => {
        // const selectedTeacherId = event.target.value as string;
        const selectedTeacher = teachers.find(teacher => teacher!.id! == selectedTeacherId);
        setFormData({
            ...formData,
            teacherId: Number(selectedTeacherId),
            teacherFirstName: selectedTeacher?.firstName || '',
            teacherLastName: selectedTeacher?.lastName || '',
        });
    };

    const handleSubmit = () => {
        if (formData.numHours <= 0 || formData.teacherId === 0) {
            enqueueSnackbar('Proszę wypełnić wszystkie pola', {variant: 'warning'});
            return;
        }
        const typeToSave = {...formData};
        console.log(typeToSave);
        onSave(typeToSave);
    };

    return (<Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>{typeData ? 'Edytuj Prowadzącego' : 'Dodaj Prowadzącego'}</DialogTitle>
            <DialogContent>
                {!typeData ? (
                        <Autocomplete
                            value={teachers.find(teacher => teacher.id === formData.teacherId)}
                            onChange={(_event, newValue) => {
                                if (newValue) {
                                    handleTeacherChange(newValue.id!);
                                }
                            }}
                            options={Object.values(teachers)}
                            getOptionLabel={(option) => `${option.firstName} ${option.lastName}`}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Prowadzący"
                                    variant="outlined"
                                    margin="normal"
                                />
                            )}
                        />) : (<TextField
                        margin="normal"
                        label="Prowadzący"
                        value={`${formData.teacherFirstName} ${formData.teacherLastName}`}
                        fullWidth
                        disabled
                    />)}
                <TextField
                    margin="normal"
                    label="Liczba godzin"
                    type="number"
                    name="numOfHours"
                    value={formData.numHours}
                    onChange={handleChange}
                    fullWidth
                    required
                />
            </DialogContent>
            <DialogActions>
                <div className={"flex"}>
                    <ActionButton onClick={handleSubmit} disabled={false}
                                  tooltipText={"Zapisz"} icon={<SaveIcon/>}
                                  colorScheme={'primary'}/>
                    <ActionButton onClick={onClose} disabled={false} tooltipText={"Anuluj"} icon={<ClearIcon/>}
                                  colorScheme={'secondary'}/>
                </div>
            </DialogActions>
        </Dialog>);
};

export default SubjectTypesTeachersModal;
