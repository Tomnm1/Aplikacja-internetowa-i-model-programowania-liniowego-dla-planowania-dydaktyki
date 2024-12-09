import React, { useState, useEffect } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions, Box
} from '@mui/material';
import {BackendSubjectType, Group} from '../utils/Interfaces';
import { useSnackbar } from 'notistack';
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";
import API_ENDPOINTS from "../app/urls.ts";
import Grid from '@mui/material/Grid2';
import {fetchWithAuth} from "../app/fetchWithAuth.ts";

interface GroupsModalProps {
    open: boolean;
    onClose: () => void;
    typeData: BackendSubjectType | null;
    semester: number;
    onSave: (typeData: BackendSubjectType, fromGroups: boolean) => void;
}
const GroupsModal: React.FC<GroupsModalProps> = ({ open, onClose, typeData,semester, onSave }) => {
    const { enqueueSnackbar } = useSnackbar();
    const [groups,setGroups] = useState<Group[]>([]);
    const [formData, setFormData] = useState<Group[]>(typeData?.groupsList || []);

    useEffect(() => {
        fetchWithAuth(`${API_ENDPOINTS.GROUPS}/semester/${semester}`)
            .then(res => res.json())
            .then((data: Group[]) => setGroups(data))
            .catch(err => {
                enqueueSnackbar(`Wystąpił błąd przy pobieraniu grup: ${err}`, { variant: 'error' });
            });
    }, [enqueueSnackbar]);

    const handleClick = (group: Group) => {
        setFormData((prev) => {
            if (prev.some(g => g.id === group.id)) {
                return prev.filter((g) => g.id !== group.id);
            }
            return [...prev, group];
        });
        console.log(formData);
    };

    const isSelected = (index: number) => formData.some(g => g.id === index);

    const handleSubmit = () => {
        const typeToSave = { ...typeData!,groupsList:formData };
        console.log(typeToSave);
        onSave(typeToSave,true);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Edytuj Przydział Grup</DialogTitle>
            <DialogContent>
                <Box my={2} sx={{textAlign: 'center'}}>Zaznacz grupy mające przedmiot</Box>
                <Grid container spacing={{ xs: 2, md: 3 }} >
                    {groups.map(g => (
                        <Grid key={g.id.toString()} size={{xs: 12, sm: 6, md: 3}}>
                            <Box
                                onClick={() => handleClick(g)}
                                sx={{
                                    textAlign: 'center',
                                    padding: 2,
                                    border: '2px inset #1a1aeb',
                                    borderRadius: '20px 20px 20px 20px',
                                    borderColor: isSelected(g.id) ? 'primary.main' : 'grey.400',
                                    backgroundColor: isSelected(g.id) ? 'primary.light' : 'transparent',
                                    cursor: 'pointer',
                                    transition: 'background-color 0.3s, border-color 0.3s',
                                    '&:hover': {
                                        backgroundColor: isSelected(g.id) ? 'primary.main' : 'grey.200',
                                    },
                                }}
                            >
                                {g.code}
                            </Box>

                        </Grid>
                    ))}
                </Grid>
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

export default GroupsModal;
