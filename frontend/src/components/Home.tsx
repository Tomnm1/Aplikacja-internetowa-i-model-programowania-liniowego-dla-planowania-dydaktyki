import React, {useCallback, useEffect, useState} from 'react';
import {
    Button, FormControl, InputLabel, LinearProgress, MenuItem, Select, SelectChangeEvent, TextField, Typography
} from '@mui/material';
import {useSnackbar} from 'notistack';
import API_ENDPOINTS from '../app/urls.ts';
import { fetchWithAuth } from '../app/fetchWithAuth.ts';

interface PlanningProgress {
    progress: number;
    status: 'IN_PROGRESS' | 'DONE' | 'ERROR';
}

const Home: React.FC = () => {
    const [planName, setPlanName] = useState('');
    const [fieldOfStudyType, setFieldOfStudyType] = useState<string>('stacjonarne');
    const [semesterType, setSemesterType] = useState<string>('zimowy');
    const [jobId, setJobId] = useState<string | null>(null);
    const [progressData, setProgressData] = useState<PlanningProgress | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        const savedJobId = localStorage.getItem('planningJobId');
        if (savedJobId) {
            setJobId(savedJobId);
        } else {
            localStorage.removeItem('planningJobId');
        }
    }, []);

    const checkProgress = useCallback(async (currentJobId: string) => {
        try {
            const res = await fetchWithAuth(API_ENDPOINTS.PLANNING_PROGRESS(currentJobId), {
                method: 'GET', headers: {'Content-Type': 'application/json'}
            });
            console.log(res);
            if (res.ok) {
                console.log(res.body);
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
                console.log(res.body);
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
        console.log(progressData, jobId)
        if (jobId === null) return;
        // if (progressData?.status !== 'IN_PROGRESS') return;
        checkProgress(jobId);
        const interval = setInterval(() => {
            checkProgress(jobId);
        }, 50000);
        return () => clearInterval(interval);
    }, [jobId, progressData, checkProgress]);


    const startPlanning = async () => {
        setLoading(true);
        try {
            const res = await fetchWithAuth(API_ENDPOINTS.START_PLANNING, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({fieldOfStudyType, semesterType, planName})
            });

            setLoading(false);
            if (res.status === 202) {
                const data = await res.json();
                const newJobId = data.jobId;
                if (newJobId) {
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

            <div className="flex gap-4 w-full max-w-md justify-center items-center flex-col">
                <div className="flex flex-row items-center">
                    <FormControl className="w-[29rem]">
                        <TextField
                            required
                            label="Nazwa planu"
                            value={planName}
                            onChange={(e) => {
                                setPlanName(e.target.value)
                            }}
                        />
                    </FormControl>
                </div>
                <div className="flex flex-row gap-4">
                    <FormControl className="w-56">
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

                    <FormControl className="w-56">
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
