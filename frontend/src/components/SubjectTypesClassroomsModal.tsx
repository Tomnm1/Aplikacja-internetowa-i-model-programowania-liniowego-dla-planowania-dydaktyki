import React, { useState, useEffect } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions, Box,
    Tabs, Tab
} from '@mui/material';
import {BackendClassroom, BackendSubjectType, BuildingDTO} from '../utils/Interfaces';
import { useSnackbar } from 'notistack';
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";
import API_ENDPOINTS from "../app/urls.ts";
import Grid from '@mui/material/Grid2';
import TabPanel from "./TabPanel.tsx";

interface SubjectTypesClassroomsModalProps {
    open: boolean;
    onClose: () => void;
    typeData: BackendSubjectType | null;
    onSave: (typeData: BackendSubjectType, fromGroups: boolean) => void;
}
const SubjectTypesClassroomsModal: React.FC<SubjectTypesClassroomsModalProps> = ({ open, onClose, typeData, onSave }) => {
    const { enqueueSnackbar } = useSnackbar();
    const [buildings,setBuildings] = useState<BuildingDTO[]>([]);
    const [value, setValue] = useState(0);
    const [formData, setFormData] = useState<BackendClassroom[]>(typeData?.classroomList || []);

    useEffect(() => {
        fetch(`${API_ENDPOINTS.BUILDINGS}/classrooms`)
            .then(res => res.json())
            .then((data: BuildingDTO[]) => setBuildings(data))
            .catch(err => {
                enqueueSnackbar(`Wystąpił błąd przy pobieraniu budynków: ${err}`, { variant: 'error' });
            });
    }, [enqueueSnackbar]);

    const handleClick = (classroom: BackendClassroom) => {
        setFormData((prev) => {
            if (prev.some(c => c.classroomID === classroom.classroomID)) {
                return prev.filter((c) => c.classroomID !== classroom.classroomID);
            }
            return [...prev, classroom];
        });
    };

    const handleChange = (_event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    const a11yProps = (index:number) => {
        return {
            id: `vertical-tab-${index}`,
            'aria-controls': `vertical-tabpanel-${index}`,
        };
    };

    const isSelected = (index: number) => formData.some(c => c.classroomID === index);

    const handleSubmit = () => {
        const typeToSave = { ...typeData!,classroomList:formData };
        onSave(typeToSave,true);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>Edytuj Przydział Sal</DialogTitle>
            <DialogContent>
                <Box my={2} sx={{textAlign: 'center'}}>Zaznacz sale mogące mieć przedmiot</Box>
                <Box
                    sx={{  bgcolor: 'background.paper', display: 'flex', width: '100%'}}
                > {/*TODO:to jest do poprawki jak będą wszystkie dane w bazie, bo no...*/ }
                    <Tabs
                        orientation="vertical"
                        variant="scrollable"
                        value={value}
                        onChange={handleChange}
                        aria-label="Vertical tabs example"
                        sx={{ borderRight: 1, borderColor: 'divider', minWidth: 100, }}
                    >
                        {buildings.map((b,i) => (
                            <Tab label={b.code} {...a11yProps(i)} />
                        ))}
                    </Tabs>
                    <Box sx={{ flexGrow: 1, overflowY: 'auto', padding: 2 }}>
                    {buildings.map((b,i) => (
                        <TabPanel value={value} index={i}>
                            <Grid container spacing={{ xs: 2, md: 3 }} >
                            {b.classroomList.map(c => (
                                <Grid key={c.classroomID!.toString()} size={{xs: 12, sm: 6, md: 3}}>
                                    <Box
                                        onClick={() => handleClick(c)}
                                        sx={{
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            textAlign: 'center',
                                            padding: 4,
                                            border: '2px inset #1a1aeb',
                                            borderRadius: '20px 20px 20px 20px',
                                            borderColor: isSelected(c.classroomID!) ? 'primary.main' : 'grey.400',
                                            backgroundColor: isSelected(c.classroomID!) ? 'primary.light' : 'transparent',
                                            cursor: 'pointer',
                                            transition: 'background-color 0.3s, border-color 0.3s',
                                            '&:hover': {
                                                backgroundColor: isSelected(c.classroomID!) ? 'primary.main' : 'grey.200',
                                            },
                                        }}
                                    >
                                        {c.code}
                                    </Box>
                                </Grid>
                            ))}
                            </Grid>
                        </TabPanel>
                    ))}
                    </Box>
                </Box>
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

export default SubjectTypesClassroomsModal;
