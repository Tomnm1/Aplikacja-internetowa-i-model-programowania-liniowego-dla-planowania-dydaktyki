import React, { useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem, OutlinedInput,
} from '@mui/material';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import { Classroom, BackendBuilding, BackendClassroom } from '../utils/Interfaces';
import { addClassroom, updateClassroom } from '../app/slices/classroomSlice';
import { API_ENDPOINTS } from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';
import { GridRowId } from '@mui/x-data-grid';

interface ClassroomModalProps {
    open: boolean;
    onClose: () => void;
    classroom: Classroom | null;
    isAdding: boolean;
}

const ClassroomModal: React.FC<ClassroomModalProps> = ({ open, onClose, classroom, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [buildings, setBuildings] = React.useState<BackendBuilding[]>([]);
    const [formData, setFormData] = React.useState<{
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

    const EQUIPMENT_OPTIONS = [
        { label: 'projektor', value: 'PROJECTOR' },
        { label: 'tablica', value: 'WHITEBOARD' },
        { label: 'komputery', value: 'COMPUTERS' },
        { label: 'nagłośnienie', value: 'SOUND' },
        { label: 'sowa', value: 'OWL'},
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
                .catch((err) => console.error('Failed to fetch buildings', err));
        } else if (classroom) {
            fetch(`${API_ENDPOINTS.BUILDINGS}/${classroom.buildingId}`)
                .then((res) => res.json())
                .then((data: BackendBuilding) => {
                    setFormData((prev) => ({
                        ...prev,
                        buildingCode: data.code,
                    }));
                })
                .catch((err) => console.error('Failed to fetch building', err));
        }
    }, [isAdding, classroom]);

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

    const handleSubmit = () => {
        if (!formData.buildingId || !formData.code) {
            alert('Please fill in all required fields.');
            return;
        }
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
        console.log(classroomData);
        if (isAdding) {
            dispatch(addClassroom(classroomData));
        } else {
            dispatch(updateClassroom(classroomData));
        }
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <DialogTitle>{isAdding ? 'Dodaj salę' : 'Szczegóły sali'}</DialogTitle>
            <DialogContent>
                {isAdding ? (
                    <FormControl fullWidth margin="normal">
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
                    //disabled={!isAdding}
                />
                <TextField
                    margin="normal"
                    label="Piętro"
                    name="floor"
                    type="number"
                    value={formData.floor}
                    onChange={handleInputChange}
                    fullWidth
                    //disabled={!isAdding}
                />
                <TextField
                    margin="normal"
                    label="Pojemność"
                    name="capacity"
                    type="number"
                    value={formData.capacity}
                    onChange={handleInputChange}
                    fullWidth
                    //disabled={!isAdding}
                />
                <FormControl fullWidth margin="normal">
                    <InputLabel id="equipment-label">Wyposażenie</InputLabel>
                    <Select
                        labelId="equipment-label"
                        id="equipment"
                        variant="outlined"
                        multiple
                        value={formData.equipment}
                        onChange={handleEquipmentChange}
                        input={<OutlinedInput label="Wyposażenie" />}
                        // disabled={!isAdding}
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
                <Button onClick={onClose}>Anuluj</Button>
                {isAdding && <Button onClick={handleSubmit} variant="contained" color="primary">Zapisz</Button>}
                {!isAdding && <Button onClick={handleSubmit} variant="contained" color="primary">Aktualizuj</Button>}
            </DialogActions>
        </Dialog>
    );
};

export default ClassroomModal;
