import React, {useCallback, useEffect, useState} from 'react';
import {
    Button, FormControl, InputLabel, LinearProgress, MenuItem, Select, SelectChangeEvent, Typography
} from '@mui/material';
import {useSnackbar} from 'notistack';
import API_ENDPOINTS from '../app/urls.ts';

interface PlanningProgress {
    progress: number;
    status: 'IN_PROGRESS' | 'DONE' | 'ERROR';
}

const Home: React.FC = () => {
    const [fieldOfStudyType, setFieldOfStudyType] = useState<string>('stacjonarne');
    const [semesterType, setSemesterType] = useState<string>('zimowy');
    const [jobId, setJobId] = useState<number | null>(null);
    const [progressData, setProgressData] = useState<PlanningProgress | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        const savedJobId = localStorage.getItem('planningJobId');
        if (savedJobId) {
            const parsedId = Number(savedJobId);
            if (!Number.isNaN(parsedId)) {
                setJobId(parsedId);
            } else {
                localStorage.removeItem('planningJobId');
            }
        }
    }, []);

    const checkProgress = useCallback(async (currentJobId: number) => {
        try {
            const res = await fetch(API_ENDPOINTS.PLANNING_PROGRESS(currentJobId), {
                method: 'GET', headers: {'Content-Type': 'application/json'}
            });
            if (res.ok) {
                const data = await res.json() as PlanningProgress;
                setProgressData(data);

                if (data.status === 'DONE') {
                    enqueueSnackbar('Plan został wygenerowany!', {variant: 'success'});
                    localStorage.removeItem('planningJobId');
                    setJobId(null);
                } else if (data.status === 'ERROR') {
                    enqueueSnackbar('Wystąpił błąd podczas generowania planu.', {variant: 'error'});
                    localStorage.removeItem('planningJobId');
                    setJobId(null);
                }
            } else if (res.status === 404) {
                enqueueSnackbar('Nie odnaleziono postępu dla zadanego jobId.', {variant: 'warning'});
                localStorage.removeItem('planningJobId');
                setJobId(null);
            } else {
                enqueueSnackbar(`Błąd pobierania postępu: ${res.status}`, {variant: 'error'});
            }
        } catch (e: any) {
            enqueueSnackbar(`Błąd: ${e.message}`, {variant: 'error'});
        }
    }, [enqueueSnackbar]);

    useEffect(() => {
        if (jobId === null) return;
        if (progressData?.status !== 'IN_PROGRESS') return;
        checkProgress(jobId);
        const interval = setInterval(() => {
            checkProgress(jobId);
        }, 5000);
        return () => clearInterval(interval);
    }, [jobId, progressData, checkProgress]);


    const startPlanning = async () => {
        setLoading(true);
        try {
            const res = await fetch(API_ENDPOINTS.START_PLANNING, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({fieldOfStudyType, semesterType})
            });

            setLoading(false);

            if (res.status === 202) {
                const data = await res.json();
                const newJobId = Number(data.jobId);
                if (!Number.isNaN(newJobId)) {
                    setJobId(newJobId);
                    localStorage.setItem('planningJobId', String(newJobId));
                    enqueueSnackbar('Rozpoczęto generowanie planu, proszę czekać.', {variant: 'info'});
                } else {
                    enqueueSnackbar('Nieprawidłowy jobId zwrócony z serwera.', {variant: 'error'});
                }
            } else {
                enqueueSnackbar('Nie udało się rozpocząć generowania planu.', {variant: 'error'});
            }

        } catch (e: any) {
            setLoading(false);
            enqueueSnackbar(`Błąd podczas rozpoczynania generowania: ${e.message}`, {variant: 'error'});
        }
    };

    return (
        <section className="flex flex-col justify-around items-center content-center gap-4 p-4">
            <Typography variant="h4">Generuj nowy plan</Typography>

            <div className="flex gap-4 w-full max-w-md justify-center items-center">
                <FormControl fullWidth>
                    <InputLabel id="fieldOfStudyType-label">Typ Studiów</InputLabel>
                    <Select
                        labelId="fieldOfStudyType-label"
                        value={fieldOfStudyType}
                        label="Typ Studiów"
                        onChange={(event: SelectChangeEvent) => setFieldOfStudyType(event.target.value)}
                    >
                        <MenuItem value="niestacjonarne">Niestacjonarne</MenuItem>
                        <MenuItem value="stacjonarne">Stacjonarne</MenuItem>
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <InputLabel id="semesterType-label">Semestr</InputLabel>
                    <Select
                        labelId="semesterType-label"
                        value={semesterType}
                        label="Semestr"
                        onChange={(event: SelectChangeEvent) => setSemesterType(event.target.value)}
                    >
                        <MenuItem value="zimowy">Zimowy</MenuItem>
                        <MenuItem value="letni">Letni</MenuItem>
                    </Select>
                </FormControl>
            </div>

            {jobId !== null && progressData && progressData.status === 'IN_PROGRESS' && (
                <div className="w-full max-w-md">
                    <Typography variant="body1" className="mb-2">
                        Generowanie w toku... ({progressData.progress}%)
                    </Typography>
                    <LinearProgress variant="determinate" value={progressData.progress}/>
                </div>
            )}

            {jobId !== null && progressData && progressData.status === 'DONE' && (
                <Typography variant="body1" color="success.main">
                    Plan został wygenerowany!
                </Typography>
            )}

            {jobId !== null && progressData && progressData.status === 'ERROR' && (
                <Typography variant="body1" color="error.main">
                    Wystąpił błąd podczas generowania planu.
                </Typography>
            )}

            {jobId === null && (
                <Button variant="contained" color="primary" onClick={startPlanning} disabled={loading}>
                    {loading ? 'Rozpoczynanie...' : 'Generuj Plan'}
                </Button>
            )}
        </section>
    );
};

export default Home;
