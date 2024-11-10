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
    Typography,
    Box,
    Fade,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import {
    SlotsDay,
    BackendSlot,
    Day,
    BackendSlotsDay,
} from '../utils/Interfaces';
import { SelectChangeEvent } from '@mui/material/Select';
import { GridRowId } from '@mui/x-data-grid';
import SaveButton from '../utils/SaveButton';
import {green} from "@mui/material/colors";
import CancelButton from "../utils/CancelButton.tsx";
import {addSlotsDay, updateSlotsDay} from "../app/slices/slotsDaysSlice.ts";
import API_ENDPOINTS from '../app/urls.ts';

interface SlotsDayModalProps {
    open: boolean;
    onClose: () => void;
    slotsDay: SlotsDay | null;
    isAdding: boolean;
}

const SlotsDayModal: React.FC<SlotsDayModalProps> = ({ open, onClose, slotsDay, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [slots, setSlots] = useState<BackendSlot[]>([]);
    const [formData, setFormData] = useState<{
        id: GridRowId;
        slotId: string;
        day:Day;
        slotRepresentation: string;
    }>({
        id: slotsDay?.id || '',
        slotId: slotsDay?.slotId ? slotsDay?.slotId.toString() : '',
        day:slotsDay?.day || Day.MONDAY,
        slotRepresentation: slotsDay?.slotRepresentation || '',
    });

    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        if (isAdding) {
            fetch(API_ENDPOINTS.SLOTS)
                .then((res) => res.json())
                .then((data: BackendSlot[]) => {
                    setSlots(data);
                    if (data.length > 0) {
                        setFormData((prev) => ({
                            ...prev,
                            slotId: data[0].slotId.toString(),
                            slotRepresentation: `${data[0].startTime} - ${data[0].endTime}`,
                        }));
                    }
                })
                .catch((err) => console.error('Failed to fetch slots', err));
        } else if (slotsDay) {
            fetch(`${API_ENDPOINTS.SLOTS}/${slotsDay.slotId}`)
                .then((res) => res.json())
                /*.then((data: BackendSlot) => {
                    setFormData((prev) => ({
                        ...prev,
                    }));
                })*/
                .catch((err) => console.error('Failed to fetch slot', err));
        }
    }, [isAdding, slotsDay]);

    useEffect(() => {
        if (slotsDay && !isAdding) {
            setFormData({
                id: slotsDay.id || '',
                slotId: slotsDay.slotId ? slotsDay.slotId.toString() : '',
                day:slotsDay.day,
                slotRepresentation:slotsDay.slotRepresentation || '',
            });
        } else if (isAdding) {
            setFormData((prev) => ({
                ...prev,
            }));
        } else {
            setFormData({
                id: '',
                slotId: '',
                day: Day.MONDAY,
                slotRepresentation: '',
            });
        }
    }, [slotsDay, isAdding, slots]);


    const handleDayChange = (event: SelectChangeEvent<Day>) => {
        const selectedDay = event.target.value as Day;
        setFormData((prev) => ({
            ...prev,
            day: selectedDay,
        }));
    };

    const handleSlotChange = (event: SelectChangeEvent) => {
        const selectedSlotId = event.target.value;
        //const selectedSlot = slots.find(s => s.slotId.toString() === selectedSlotId);
        setFormData((prev) => ({
            ...prev,
            slotId: selectedSlotId,
        }));
    };

    const handleSubmit = async () => {
        if (!formData.slotId) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }

        setLoading(true);
        setSuccess(false);

        const slotsDayData: BackendSlotsDay = {
            day: formData.day,
            slot: {
                slotId: Number(formData.slotId),
                startTime: '',
                endTime: ''
            },
        };

        if (!isAdding && formData.id !== '') {
            slotsDayData.SlotsDayId = Number(formData.id);
        }

        const MIN_SUCCESS_DURATION = 1000;
        const startTime = Date.now();

        try {
            if (isAdding) {
                await dispatch(addSlotsDay(slotsDayData)).unwrap();
            } else {
                await dispatch(updateSlotsDay(slotsDayData)).unwrap();
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
        } catch (error) {
            alert('Wystąpił błąd podczas zapisywania slotu dnia.');
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
                            <DialogTitle>{isAdding ? 'Dodaj slot dnia' : 'Szczegóły slotu dnia'}</DialogTitle>
                            <DialogContent>
                                {isAdding ? (
                                    <FormControl fullWidth margin="normal" disabled={loading}>
                                        <InputLabel id="slot-label">Slot</InputLabel>
                                        <Select
                                            labelId="slot-label"
                                            value={formData.slotId}
                                            onChange={handleSlotChange}
                                            label="Slot"
                                            variant="outlined"
                                        >
                                            {slots.map((slot) => (
                                                <MenuItem key={slot.slotId} value={slot.slotId.toString()}>
                                                    {`${slot.startTime} - ${slot.endTime}` || `Slot ${slot.slotId}`}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </FormControl>
                                ) : (
                                    <TextField
                                        margin="normal"
                                        label="Slot"
                                        value={formData.slotRepresentation}
                                        fullWidth
                                        disabled
                                    />
                                )}
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="day-label">Dzień</InputLabel>
                                    <Select
                                        labelId="day-label"
                                        id="day"
                                        variant="outlined"
                                        value={formData.day}
                                        onChange={handleDayChange}
                                    >
                                        <MenuItem value={Day.MONDAY}>Poniedziałek</MenuItem>
                                        <MenuItem value={Day.TUESDAY}>Wtorek</MenuItem>
                                        <MenuItem value={Day.WEDNESDAY}>Środa</MenuItem>
                                        <MenuItem value={Day.THURSDAY}>Czwartek</MenuItem>
                                        <MenuItem value={Day.FRIDAY}>Piątek</MenuItem>
                                        <MenuItem value={Day.SATURDAY}>Sobota</MenuItem>
                                        <MenuItem value={Day.SUNDAY}>Niedziela</MenuItem>
                                    </Select>
                                </FormControl>
                            </DialogContent>
                            <DialogActions>
                                <SaveButton onClick={handleSubmit} loading={loading} success={success} />
                                <CancelButton onClick={onClose} disabled={loading} />
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

export default SlotsDayModal;
