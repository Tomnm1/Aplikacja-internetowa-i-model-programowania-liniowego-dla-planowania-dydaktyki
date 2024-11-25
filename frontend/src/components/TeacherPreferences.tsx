import React, {useCallback} from 'react';
import {
    Box, CircularProgress, Table, TableBody, TableCell, TableHead, TableRow, Tooltip, Typography,
} from '@mui/material';
import {Day, dayMapping, Slot} from '../utils/Interfaces';
import {blueGrey, green, red} from "@mui/material/colors";

interface TeacherPreferencesProps {
    slots: Slot[];
    preferences: Record<string, 'neutral' | 'green' | 'red'>;
    setPreferences: React.Dispatch<React.SetStateAction<Record<string, 'neutral' | 'green' | 'red'>>>;
    loading: boolean;
}

type SlotPreference = 'neutral' | 'green' | 'red';

const TeacherPreferences: React.FC<TeacherPreferencesProps> = ({slots, preferences, setPreferences, loading}) => {

    const daysOfWeek: Day[] = Object.values(Day);

    const handleSlotClick = useCallback((day: Day, slotId: string) => {
        if (loading) return;

        const key = `${day}-${slotId}`;
        setPreferences((prev) => {
            const current = prev[key] || 'neutral';
            let next: SlotPreference;

            if (current === 'green') {
                next = 'neutral';
            } else {
                next = 'green';
                if (current === 'red') {
                    return {
                        ...prev, [key]: 'green',
                    };
                }
            }
            return {
                ...prev, [key]: next,
            };
        });
    }, [loading, setPreferences]);

    const handleSlotRightClick = useCallback((e: React.MouseEvent, day: Day, slotId: string) => {
        e.preventDefault();
        if (loading) return;

        const key = `${day}-${slotId}`;
        setPreferences((prev) => {
            const current = prev[key] || 'neutral';
            let next: SlotPreference;

            if (current === 'red') {
                next = 'neutral';
            } else {
                next = 'red';
                if (current === 'green') {
                    return {
                        ...prev, [key]: 'red',
                    };
                }
            }

            return {
                ...prev, [key]: next,
            };
        });
    }, [loading, setPreferences]);

    const getSlotColor = (preference: SlotPreference) => {
        if (preference === 'green') return green[400];
        if (preference === 'red') return red[400];
        return blueGrey[200];
    };

    return (<Box>
            <Typography variant="h6" gutterBottom>
                Wybierz preferowane godziny
            </Typography>
            {loading && (<Box display="flex" justifyContent="center" alignItems="center" my={2}>
                    <CircularProgress/>
                </Box>)}
            <Box overflow="auto">
                <Table>
                    <TableHead>
                        <TableRow>
                            {daysOfWeek.map((day) => (<TableCell
                                    key={day}
                                    align="center"
                                    sx={{
                                        fontWeight: 'bold', minWidth: '120px', backgroundColor: '#f5f5f5',
                                    }}
                                >
                                    {dayMapping[day]}
                                </TableCell>))}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {slots.map((slot) => (<TableRow key={slot.slot_id}>
                                {daysOfWeek.map((day) => {
                                    const slotIdStr = slot.slot_id.toString();
                                    const key = `${day}-${slotIdStr}`;
                                    const preference = preferences[key] || 'neutral';
                                    return (<TableCell key={key} align="center" sx={{padding: '8px'}}>
                                            <Tooltip
                                                title={`${dayMapping[day]} ${slot.start_time.slice(0, 5)} - ${slot.end_time.slice(0, 5)} (${preference === 'green' ? 'Preferowane' : preference === 'red' ? 'Niepreferowane' : 'Neutralne'})`}>
                                                <Box
                                                    onClick={() => handleSlotClick(day, slotIdStr)}
                                                    onContextMenu={(e) => handleSlotRightClick(e, day, slotIdStr)}
                                                    sx={{
                                                        width: '100%',
                                                        height: '3.5rem',
                                                        backgroundColor: getSlotColor(preference),
                                                        border: '1px solid #000',
                                                        borderRadius: '4px',
                                                        cursor: loading ? 'not-allowed' : 'pointer',
                                                        display: 'flex',
                                                        alignItems: 'center',
                                                        justifyContent: 'center',
                                                        userSelect: 'none',
                                                        opacity: loading ? 0.6 : 1,
                                                        transition: 'background-color 0.3s, opacity 0.3s',
                                                        '&:hover': {
                                                            opacity: loading ? 0.6 : 0.8,
                                                        },
                                                    }}
                                                >
                                                    <Typography variant="body2" color="#fff" fontWeight="semibold">
                                                        {slot.start_time.slice(0, 5)} - {slot.end_time.slice(0, 5)}
                                                    </Typography>
                                                </Box>
                                            </Tooltip>
                                        </TableCell>);
                                })}
                            </TableRow>))}
                    </TableBody>
                </Table>
            </Box>
            <Box display="flex" gap={4} mt={3} justifyContent="center" flexWrap="wrap">
                <Box display="flex" alignItems="center">
                    <Box
                        sx={{
                            width: '20px',
                            height: '20px',
                            backgroundColor: green[500],
                            mr: 1,
                            border: '1px solid #000',
                            borderRadius: '2px',
                        }}
                    />
                    <Typography>Zielony – Preferowane</Typography>
                </Box>
                <Box display="flex" alignItems="center">
                    <Box
                        sx={{
                            width: '20px',
                            height: '20px',
                            backgroundColor: red[500],
                            mr: 1,
                            border: '1px solid #000',
                            borderRadius: '2px',
                        }}
                    />
                    <Typography>Czerwony – Niepreferowane</Typography>
                </Box>
                <Box display="flex" alignItems="center">
                    <Box
                        sx={{
                            width: '20px',
                            height: '20px',
                            backgroundColor: blueGrey[200],
                            mr: 1,
                            border: 'none',
                            borderRadius: '2px',
                        }}
                    />
                    <Typography>Szary – Neutralne</Typography>
                </Box>
            </Box>
        </Box>);
};

export default TeacherPreferences;
