import React, { useState, useEffect } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
} from '@mui/material';
import { BackendSubjectType, Type, typeMapping } from '../utils/Interfaces';
import { useSnackbar } from 'notistack';
import { SelectChangeEvent } from '@mui/material/Select';
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";

interface TypeModalProps {
    open: boolean;
    onClose: () => void;
    typeData: BackendSubjectType | null;
    onSave: (typeData: BackendSubjectType, fromGroups: boolean) => void;
}
//TODO zabezpieczyć przed wielokrotnym dodaniem tego samego typu przedmiotu -> np 2x wykład etc
const TypeModal: React.FC<TypeModalProps> = ({ open, onClose, typeData, onSave }) => {
    const { enqueueSnackbar } = useSnackbar();
    const [formData, setFormData] = useState<BackendSubjectType>({
        numOfHours: typeData?.numOfHours || 0,
        type: typeData?.type || Type.LECTURE,
        maxStudentsPerGroup: typeData?.maxStudentsPerGroup || 0,
        subject: typeData?.subject || { SubjectId: 0 },
        teachersList: typeData?.teachersList || [],
        groupsList: typeData?.groupsList || [],
        frontId: typeData?.frontId || `ID-${Date.now()}`,
    });

    useEffect(() => {
        if (typeData) {
            setFormData(typeData);
        } else {
            setFormData({
                numOfHours: 0,
                type: Type.LECTURE,
                maxStudentsPerGroup: 0,
                subject: { SubjectId: 0 },
                teachersList: [],
                groupsList: [],
                frontId: `ID-${Date.now()}`,
            });
        }
    }, [typeData]);

    const handleChange = (event: SelectChangeEvent<Type> | React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> ) => {
        const { name, value } = event.target;
        setFormData((prev) => ({
            ...prev,
            [name as string]: value,
        }));
    };

    const handleSubmit = () => {
        if (formData.numOfHours <= 0 || formData.maxStudentsPerGroup <= 0) {
            enqueueSnackbar('Proszę wypełnić wszystkie pola', { variant: 'warning' });
            return;
        }
        const typeToSave = { ...formData };
        onSave(typeToSave,false);
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>{typeData ? 'Edytuj Typ Przedmiotu' : 'Dodaj Typ Przedmiotu'}</DialogTitle>
            <DialogContent>
                <FormControl fullWidth margin="normal">
                    <InputLabel id="type-label">Typ</InputLabel>
                    <Select
                        labelId="type-label"
                        name="type"
                        value={formData.type}
                        onChange={handleChange}
                        label="Typ"
                    >
                        {Object.values(Type).map((type) => (
                            <MenuItem key={type} value={type}>
                                {typeMapping[type]}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <TextField
                    margin="normal"
                    label="Liczba godzin"
                    type="number"
                    name="numOfHours"
                    value={formData.numOfHours}
                    onChange={handleChange}
                    fullWidth
                    required
                />
                <TextField
                    margin="normal"
                    label="Maksymalna liczba studentów na grupę"
                    type="number"
                    name="maxStudentsPerGroup"
                    value={formData.maxStudentsPerGroup}
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
        </Dialog>
    );
};

export default TypeModal;
