import React, { useEffect, useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    OutlinedInput,
    Typography,
    Box,
    Fade,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { Classroom, BackendBuilding, BackendClassroom } from '../utils/Interfaces';
import { addClassroom, updateClassroom } from '../app/slices/classroomSlice';
import { API_ENDPOINTS } from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';
import { GridRowId } from '@mui/x-data-grid';
import {green} from "@mui/material/colors";
import {useSnackbar} from "notistack";
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";

interface ClassroomModalProps {
    open: boolean;
    onClose: () => void;
    classroom: Classroom | null;
    isAdding: boolean;
}

const ClassroomModal: React.FC<ClassroomModalProps> = ({ open, onClose, classroom, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const { enqueueSnackbar } = useSnackbar();
    const [buildings, setBuildings] = useState<BackendBuilding[]>([]);
    const [formData, setFormData] = useState<{
        id: GridRowId;
        buildingId: string;
        buildingCode: string;
        code: string;
        floor: number;
        capacity: number;
        equipment: string[];
    }>({
        id: classroom?.id || '',
        buildingId: classroom?.buildingId ? classroom.buildingId.toString() : '',
        buildingCode: '',
        code: classroom?.code || '',
        floor: classroom?.floor || 0,
        capacity: classroom?.capacity || 0,
        equipment: classroom?.equipment || [],
    });

    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    const EQUIPMENT_OPTIONS = [
        { label: 'projektor', value: 'PROJECTOR' },
        { label: 'tablica', value: 'WHITEBOARD' },
        { label: 'komputery', value: 'COMPUTERS' },
        { label: 'nagłośnienie', value: 'SOUND' },
        { label: 'sowa', value: 'OWL' },
    ];

    useEffect(() => {
        if (isAdding) {
            fetch(API_ENDPOINTS.BUILDINGS)
                .then((res) => res.json())
                .then((data: BackendBuilding[]) => {
                    setBuildings(data);
                    if (data.length > 0) {
                        setFormData((prev) => ({
                            ...prev,
                            buildingId: data[0].buildingId.toString(),
                            buildingCode: data[0].code,
                        }));
                    }
                })
                .catch(err => {
                    enqueueSnackbar(`Wystąpił błąd przy pobieraniu budynków: ${err}`, { variant: 'error' });
                });
        } else if (classroom) {
            fetch(`${API_ENDPOINTS.BUILDINGS}/${classroom.buildingId}`)
                .then((res) => res.json())
                .then((data: BackendBuilding) => {
                    setFormData((prev) => ({
                        ...prev,
                        buildingCode: data.code,
                    }));
                })
                .catch(err => {
                    enqueueSnackbar(`Wystąpił błąd przy pobieraniu budynku nr ${classroom.buildingId}$: ${err}`, { variant: 'error' });
                });
        }
    }, [isAdding, classroom, enqueueSnackbar]);

    useEffect(() => {
        if (classroom && !isAdding) {
            setFormData({
                id: classroom.id || '',
                buildingId: classroom.buildingId ? classroom.buildingId.toString() : '',
                buildingCode: '',
                code: classroom.code,
                floor: classroom.floor,
                capacity: classroom.capacity,
                equipment: classroom.equipment || [],
            });
        } else if (isAdding) {
            setFormData((prev) => ({
                ...prev,
                buildingCode: buildings.find(b => b.buildingId.toString() === prev.buildingId)?.code || '',
            }));
        } else {
            setFormData({
                id: '',
                buildingId: '',
                buildingCode: '',
                code: '',
                floor: 0,
                capacity: 0,
                equipment: [],
            });
        }
    }, [classroom, isAdding, buildings]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === 'floor' || name === 'capacity' ? Number(value) : value,
        }));
    };

    const handleEquipmentChange = (event: SelectChangeEvent<typeof formData.equipment>) => {
        const {
            target: { value },
        } = event;
        setFormData((prev) => ({
            ...prev,
            equipment: typeof value === 'string' ? value.split(',') : value,
        }));
    };

    const handleBuildingChange = (event: SelectChangeEvent) => {
        const selectedBuildingId = event.target.value;
        const selectedBuilding = buildings.find(b => b.buildingId.toString() === selectedBuildingId);
        setFormData((prev) => ({
            ...prev,
            buildingId: selectedBuildingId,
            buildingCode: selectedBuilding ? selectedBuilding.code : '',
        }));
    };

    const handleSubmit = async () => {
        if (!formData.buildingId || !formData.code) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", { variant: 'warning' });
            return;
        }

        setLoading(true);
        setSuccess(false);

        const equipmentMap: { [key: string]: boolean } = {};
        formData.equipment.forEach((key) => {
            equipmentMap[key] = true;
        });

        const classroomData: BackendClassroom = {
            code: formData.code,
            floor: formData.floor,
            capacity: formData.capacity,
            equipment: equipmentMap,
            building: {
                buildingId: Number(formData.buildingId),
                code: formData.buildingCode,
            },
        };

        if (!isAdding && formData.id !== '') {
            classroomData.classroomID = Number(formData.id);
        }

        const MIN_SUCCESS_DURATION = 1000;
        const startTime = Date.now();

        try {
            if (isAdding) {
                await dispatch(addClassroom(classroomData)).unwrap();
            } else {
                await dispatch(updateClassroom(classroomData)).unwrap();
            }
            setSuccess(true);
            const elapsedTime = Date.now() - startTime;
            const remainingTime = MIN_SUCCESS_DURATION - elapsedTime;
            if (remainingTime > 0) {
                setTimeout(() => {
                    setLoading(false);
                    setSuccess(false);
                    onClose();
                }, remainingTime);
            } else {
                setLoading(false);
                setSuccess(false);
                onClose();
            }
        } catch (error : any) {
            enqueueSnackbar(`Wystąpił błąd przy ${isAdding ? 'dodawaniu' : 'aktualizacji'} rekordu: ${error.message || error}`, { variant: 'error' });
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box
                    sx={{
                        position: 'relative',
                        transition: 'background-color 0.5s ease',
                        backgroundColor: success ? 'white' : 'inherit',
                        minHeight: '200px',
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center',
                        alignItems: 'center',
                        padding: success ? 4 : undefined,
                    }}
                >
                    {!success ? (
                        <>
                            <DialogTitle>{isAdding ? 'Dodaj salę' : 'Szczegóły sali'}</DialogTitle>
                            <DialogContent>
                                {isAdding ? (
                                    <FormControl fullWidth margin="normal" disabled={loading}>
                                        <InputLabel id="building-label">Budynek</InputLabel>
                                        <Select
                                            labelId="building-label"
                                            value={formData.buildingId}
                                            onChange={handleBuildingChange}
                                            label="Budynek"
                                            variant="outlined"
                                        >
                                            {buildings.map((building) => (
                                                <MenuItem key={building.buildingId} value={building.buildingId.toString()}>
                                                    {building.code || `Budynek ${building.buildingId}`}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                ) : (
                                    <TextField
                                        margin="normal"
                                        label="Budynek"
                                        value={formData.buildingCode}
                                        fullWidth
                                        disabled
                                    />
                                )}
                                <TextField
                                    margin="normal"
                                    label="Kod"
                                    name="code"
                                    value={formData.code}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <TextField
                                    margin="normal"
                                    label="Piętro"
                                    name="floor"
                                    type="number"
                                    value={formData.floor}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <TextField
                                    margin="normal"
                                    label="Pojemność"
                                    name="capacity"
                                    type="number"
                                    value={formData.capacity}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="equipment-label">Wyposażenie</InputLabel>
                                    <Select
                                        labelId="equipment-label"
                                        id="equipment"
                                        variant="outlined"
                                        multiple
                                        value={formData.equipment}
                                        onChange={handleEquipmentChange}
                                        input={<OutlinedInput label="Wyposażenie" />}
                                    >
                                        {EQUIPMENT_OPTIONS.map((option) => (
                                            <MenuItem
                                                key={option.value}
                                                value={option.value}
                                            >
                                                {option.label}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </DialogContent>
                            <DialogActions>
                                <div className={"flex"}>
                                    <ActionButton onClick={handleSubmit} disabled={loading}
                                                  tooltipText={isAdding ? 'Dodaj' : 'Zaktualizuj'} icon={<SaveIcon/>}
                                                  colorScheme={'primary'}/>
                                    <ActionButton onClick={onClose} disabled={loading} tooltipText={"Anuluj"}
                                                  icon={<ClearIcon/>} colorScheme={'secondary'}/>
                                </div>
                            </DialogActions>
                        </>
                    ) : (
                        <Fade in={success} timeout={1000}>
                            <Box
                                sx={{
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                }}
                            >
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

export default ClassroomModal;
