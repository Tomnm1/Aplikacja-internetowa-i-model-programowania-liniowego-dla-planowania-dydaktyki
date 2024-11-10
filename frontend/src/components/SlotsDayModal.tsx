import React, { useEffect, useState } from 'react';
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
    Typography, Box, Fade,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import { useDispatch } from 'react-redux';
import { AppDispatch } from '../app/store';
import {SlotsDay, BackendSlot, Day, dayMapping} from '../utils/Interfaces';
import SaveButton from '../utils/SaveButton';
import { green } from "@mui/material/colors";
import CancelButton from "../utils/CancelButton";
import {addSlotsDay, fetchSlotsDays, updateSlotsDay} from "../app/slices/slotsDaysSlice";
import API_ENDPOINTS from '../app/urls';
import { SelectChangeEvent } from '@mui/material/Select';

interface SlotsDayModalProps {
    open: boolean;
    onClose: () => void;
    slotsDay: SlotsDay | null;
    isAdding: boolean;
}

const SlotsDayModal: React.FC<SlotsDayModalProps> = ({ open, onClose, slotsDay, isAdding }) => {
    const dispatch = useDispatch<AppDispatch>();
    const [slots, setSlots] = useState<BackendSlot[]>([]);
    const [formData, setFormData] = useState({
        id: slotsDay?.id || '',
        slotId: slotsDay?.slotId?.toString() || '',
        day: slotsDay?.day || Day.MONDAY,
        slotRepresentation: slotsDay?.slotRepresentation || '',
    });
    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        if (isAdding) {
            fetch(API_ENDPOINTS.SLOTS)
                .then(res => res.json())
                .then((data: BackendSlot[]) => setSlots(data))
                .catch(err => console.error('Failed to fetch slots', err));
        }
    }, [isAdding]);

    const handleDayChange = (event: SelectChangeEvent) => {
        setFormData({ ...formData, day: event.target.value as Day });
    };

    const handleSlotChange = (event: SelectChangeEvent) => {
        const selectedSlotId = event.target.value as string;
        const selectedSlot = slots.find(slot => slot.slotId.toString() === selectedSlotId);
        setFormData({
            ...formData,
            slotId: selectedSlotId,
            slotRepresentation: selectedSlot ? `${selectedSlot.startTime} - ${selectedSlot.endTime}` : '',
        });
    };

    const handleSubmit = async () => {
        if (!formData.slotId) {
            alert('Proszę wypełnić wszystkie pola.');
            return;
        }

        setLoading(true);

        const slotsDayData = {
            day: formData.day,
            slot: {
                slotId: Number(formData.slotId),
            },
            ...(isAdding ? {} : { SlotsDayId: Number(formData.id) }),
        };

        try {
            const action = isAdding ? addSlotsDay : updateSlotsDay;
            await dispatch(action(slotsDayData)).unwrap();
            await dispatch(fetchSlotsDays());
            setSuccess(true);
            setTimeout(() => {
                setLoading(false);
                setSuccess(false);
                onClose();
            }, 1000);
        } catch (error) {
            console.log(error)
            alert('Wystąpił błąd podczas zapisywania slotu dnia.');
            setLoading(false);
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box sx={{ position: 'relative', minHeight: '200px', padding: 4 }}>
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
                                                    {`${slot.startTime} - ${slot.endTime}`}
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
                                        value={formData.day}
                                        onChange={handleDayChange}
                                        label="Dzień"
                                        variant="outlined"
                                    >
                                        {Object.values(Day).map((day) => (
                                            <MenuItem key={day} value={day}>
                                                {dayMapping[day]}
                                            </MenuItem>
                                        ))}
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
                            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
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
