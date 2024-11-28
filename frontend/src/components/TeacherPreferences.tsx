import React, {useCallback, useEffect, useMemo} from 'react';
import {
    Box, CircularProgress, Table, TableBody, TableCell, TableHead, TableRow, Tooltip, Typography,
} from '@mui/material';
import {useDispatch, useSelector} from 'react-redux';
import {AppDispatch, RootState} from '../app/store';
import {blueGrey, green, red} from '@mui/material/colors';
import {Day, dayMapping, SlotPreference, SlotsDay} from '../utils/Interfaces';
import {fetchSlotsDays} from '../app/slices/slotsDaysSlice';
import {useSnackbar} from 'notistack';

interface TeacherPreferencesProps {
    preferences: Record<number, 0 | 1 | -1>;
    setPreferences: React.Dispatch<React.SetStateAction<Record<number, 0 | 1 | -1>>>;
    loading: boolean;
}

const TeacherPreferences: React.FC<TeacherPreferencesProps> = ({preferences, setPreferences, loading}) => {
    const {enqueueSnackbar} = useSnackbar();
    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        dispatch(fetchSlotsDays())
            .unwrap()
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania slotów dnia: ${error.message}`, {variant: 'error'});
            });
    }, [dispatch, enqueueSnackbar]);

    const slotsDays = useSelector((state: RootState) => state.slotsDays.rows);
    const days = useMemo(() => Object.values(Day), []);

    const uniqueSlots = useMemo(() => {
        const slotSet = new Set<string>();
        slotsDays.forEach((slotDay) => {
            if (slotDay.slotRepresentation) {
                slotSet.add(slotDay.slotRepresentation);
            }
        });
        return Array.from(slotSet).sort((a, b) => a.localeCompare(b));
    }, [slotsDays]);

    const slotMap: Record<string, Record<Day, SlotsDay | undefined>> = useMemo(() => {
        const map: Record<string, Record<Day, SlotsDay | undefined>> = {};
        uniqueSlots.forEach((slotRep) => {
            map[slotRep] = {} as Record<Day, SlotsDay | undefined>;
            days.forEach((day) => {
                map[slotRep][day] = slotsDays.find((slotDay) => slotDay.slotRepresentation === slotRep && slotDay.day === day);
            });
        });
        return map;
    }, [uniqueSlots, slotsDays, days]);
    ``
    const handleSlotClick = useCallback((e: React.MouseEvent, id: number, pref: SlotPreference) => {
        e.preventDefault();
        if (loading) return;
        setPreferences((prev) => {
            const current = prev[id] || 0;
            const next: SlotPreference = current === pref ? 0 : pref;
            return {...prev, [id]: next};
        });
    }, [loading, setPreferences]);

    const getSlotColor = (preference: SlotPreference) => {
        if (preference === 1) return green[400];
        if (preference === -1) return red[400];
        return blueGrey[200];
    };

    return (<Box>
        <Typography variant="h6" gutterBottom>
            Wybierz preferowane godziny
        </Typography>
        {loading && (<Box display="flex" justifyContent="center" alignItems="center" my={2}>
            <CircularProgress/>
        </Box>)}
        {!loading && slotsDays.length === 0 && (<Typography variant="body1" color="textSecondary">
            Brak dostępnych slotów.
        </Typography>)}
        {!loading && slotsDays.length > 0 && (<Box overflow="auto">
            <Table>
                <TableHead>
                    <TableRow>
                        {days.map((day) => (<TableCell
                            key={day}
                            align="center"
                            sx={{
                                fontWeight: 'bold', minWidth: '120px',
                            }}
                        >
                            {dayMapping[day]}
                        </TableCell>))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {uniqueSlots.map((slotRep) => (<TableRow key={slotRep}>
                        {days.map((day) => {
                            const slotDay = slotMap[slotRep][day];
                            const preference = slotDay ? (preferences[slotDay.id] || 0) : 0;

                            return (<TableCell key={`${day}-${slotRep}`} align="center" sx={{padding: '8px'}}>
                                {slotDay ? (<Tooltip
                                    title={`${dayMapping[day]} ${slotRep} (${preference === 1 ? 'Preferowane' : preference === -1 ? 'Niepreferowane' : 'Neutralne'})`}
                                >
                                    <Box
                                        onClick={(e) => handleSlotClick(e, slotDay.id, 1)}
                                        onContextMenu={(e) => handleSlotClick(e, slotDay.id, -1)}
                                        sx={{
                                            width: '100%',
                                            height: '3.5em',
                                            backgroundColor: getSlotColor(preference),
                                            border: 'none',
                                            borderRadius: '4px',
                                            cursor: !loading ? 'pointer' : 'not-allowed',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            userSelect: 'none',
                                            opacity: loading ? 0.6 : 1,
                                            transition: 'background-color 0.3s, opacity 0.3s',
                                            '&:hover': {
                                                opacity: !loading ? 0.8 : 0.6,
                                            },
                                        }}
                                    >
                                        <Typography variant="body2" color="#fff"
                                                    fontWeight="semibold">
                                            {`${slotRep.slice(0, 5)} - ${slotRep.slice(11, 16)}`}
                                        </Typography>
                                    </Box>
                                </Tooltip>) : (<Box
                                    sx={{
                                        width: '100%',
                                        height: '3.5em',
                                        backgroundColor: blueGrey[200],
                                        border: 'none',
                                        borderRadius: '4px',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        userSelect: 'none',
                                        opacity: 0.6,
                                    }}
                                >
                                    <Typography variant="body2" color="textSecondary">
                                        brak
                                    </Typography>
                                </Box>)}
                            </TableCell>);
                        })}
                    </TableRow>))}
                </TableBody>
            </Table>
        </Box>)}
        <Box display="flex" gap={4} mt={3} justifyContent="center" flexWrap="wrap">
            <Box display="flex" alignItems="center">
                <Box
                    sx={{
                        width: '20px',
                        height: '20px',
                        backgroundColor: green[500],
                        mr: 1,
                        border: 'none',
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
                        border: 'none',
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
