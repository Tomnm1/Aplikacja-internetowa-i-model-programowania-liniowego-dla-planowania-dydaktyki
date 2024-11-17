import React, { useState } from 'react';
import {
    Step,
    StepLabel,
    Stepper,
    CircularProgress,
    Typography,
    Snackbar,
    Alert,
    Button,
} from '@mui/material';
import {
    CheckCircle as CheckCircleIcon,
    Error as ErrorIcon,
    Build as BuildIcon,
    VerifiedUser as VerifiedUserIcon,
} from '@mui/icons-material';
import ActionButton from "../utils/ActionButton.tsx";
import NavigateNextIcon from "@mui/icons-material/NavigateNext";

const steps = ['Sprawdź ograniczenia', 'Budowanie', 'Sukces lub Błąd'];

const dummyProcesses = [
    'Weryfikacja danych',
    'Obliczanie',
    'Zbieranie obliczeń',
    'Tworzenie planu',
    'Walidacja wyników',
];

type SnackbarState = {
    open: boolean;
    message: string;
    severity: 'success' | 'error' | 'info' | 'warning';
};

const Home: React.FC = () => {
    const [activeStep, setActiveStep] = useState<number>(0);
    const [buildingStep, setBuildingStep] = useState<number>(0);
    const [checkLoading, setCheckLoading] = useState<boolean>(false);
    const [checkSuccess, setCheckSuccess] = useState<boolean>(false);
    const [buildLoading, setBuildLoading] = useState<boolean>(false);
    const [buildSuccess, setBuildSuccess] = useState<boolean>(false);
    const [result, setResult] = useState<'success' | 'error' | null>(null);
    const [error, setError] = useState<string>('');
    const [snackbar, setSnackbar] = useState<SnackbarState>({
        open: false,
        message: '',
        severity: 'success',
    });

    const handleNext = () => {
        setActiveStep((prev) => prev + 1);
    };

    const handleReset = () => {
        setActiveStep(0);
        setBuildingStep(0);
        setCheckLoading(false);
        setCheckSuccess(false);
        setBuildLoading(false);
        setBuildSuccess(false);
        setResult(null);
        setError('');
    };

    const checkRestrictions = async () => {
        setCheckLoading(true);
        setError('');
        try {
            //tu słać requesta, kiedyś
            await new Promise((resolve) => setTimeout(resolve, 2000));
            setCheckSuccess(true);
            setSnackbar({ open: true, message: 'Ograniczenia sprawdzone pomyślnie!', severity: 'success' });
            handleNext();
        } catch (err) {
            setError('Wystąpił błąd podczas sprawdzania ograniczeń.');
            setSnackbar({ open: true, message: 'Błąd podczas sprawdzania ograniczeń.', severity: 'error' });
        } finally {
            setCheckLoading(false);
        }
    };

    const startBuilding = async () => {
        setBuildLoading(true);
        setBuildSuccess(false);
        setBuildingStep(0);
        setError('');
        for (let i = 0; i < dummyProcesses.length; i++) {
            await new Promise((resolve) => setTimeout(resolve, 1000));
            setBuildingStep((prev) => prev + 1);
        }
        const isSuccess = Math.random() > 0.3;
        if (isSuccess) {
            setBuildSuccess(true);
            setResult('success');
            setSnackbar({ open: true, message: 'Budowanie zakończone sukcesem!', severity: 'success' });
        } else {
            setBuildSuccess(false);
            setResult('error');
            setError('Budowanie zakończyło się błędem.');
            setSnackbar({ open: true, message: 'Budowanie zakończyło się błędem.', severity: 'error' });
        }
        setBuildLoading(false);
        handleNext();
    };

    const getStepContent = (step: number) => {
        switch (step) {
            case 0:
                return (
                    <div className="flex flex-col items-center justify-center p-6 bg-white rounded-lg shadow-md">
                        <Typography variant="h6" className="mb-4">
                            Sprawdź Ograniczenia
                        </Typography>
                        <ActionButton onClick={checkRestrictions} disabled={checkLoading}
                                      tooltipText={'Przejdź do następnego kroku'} icon={<NavigateNextIcon/>}
                                      colorScheme={'primary'} />
                        {error && (
                            <Typography color="error" className="mt-4">
                                {error}
                            </Typography>
                        )}
                    </div>
                );
            case 1:
                return (
                    <div className="flex flex-col items-center justify-center p-6 bg-white rounded-lg shadow-md">
                        <Typography variant="h6" className="mb-4">
                            Budowanie
                        </Typography>
                        {!buildLoading && !buildSuccess ? (
                            <ActionButton onClick={startBuilding} disabled={buildLoading}
                                          tooltipText={'Zbuduj'} icon={<NavigateNextIcon/>}
                                          colorScheme={'primary'} />
                        ) : (
                            <div className="w-full mt-6">
                                <div className="bg-gray-100 p-4 rounded-lg shadow-inner">
                                    <Stepper activeStep={buildingStep} orientation="vertical">
                                        {dummyProcesses.map((process) => (
                                            <Step key={process}>
                                                <StepLabel>{process}</StepLabel>
                                            </Step>
                                        ))}
                                    </Stepper>
                                    <div className="flex justify-center mt-4">
                                        <CircularProgress />
                                    </div>
                                    <Typography variant="body2" className="mt-2 text-center">
                                        Proces budowania...
                                    </Typography>
                                </div>
                            </div>
                        )}
                        {error && (
                            <Typography color="error" className="mt-4">
                                {error}
                            </Typography>
                        )}
                    </div>
                );
            case 2:
                return (
                    <div className="flex flex-col items-center justify-center p-6 bg-white rounded-lg shadow-md">
                        {result === 'success' ? (
                            <>
                                <CheckCircleIcon className="text-green-500" style={{ fontSize: 80 }} />
                                <Typography variant="h5" className="mt-4">
                                    Operacja zakończona sukcesem!
                                </Typography>
                            </>
                        ) : (
                            <>
                                <ErrorIcon className="text-red-500" style={{ fontSize: 80 }} />
                                <Typography variant="h5" className="mt-4">
                                    Wystąpił błąd podczas operacji.
                                </Typography>
                                {error && (
                                    <Typography color="error" className="mt-2">
                                        {error}
                                    </Typography>
                                )}
                            </>
                        )}
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleReset}
                            size="large"
                            className="mt-6 px-6 py-3"
                        >
                            Rozpocznij od nowa
                        </Button>
                    </div>
                );
            default:
                return 'Nieznany krok';
        }
    };

    return (
        <div className="min-h-full flex flex-col items-center justify-center p-4">
            {/*todo work in progress, ale jak ktoś chce może dokończyć  :33 */}
            <p>work in progress, ale jak ktoś chce może dokończyć :33</p>
            <div className="w-full max-w-4xl">
                <div className="bg-white rounded-lg shadow-lg p-6">
                    <Stepper activeStep={activeStep} alternativeLabel>
                        {steps.map((label, index) => (
                            <Step key={label}>
                                <StepLabel
                                    StepIconComponent={() => {
                                        if (activeStep > index) {
                                            if (index === steps.length - 1) {
                                                return result === 'success' ? (
                                                    <CheckCircleIcon className="text-green-500" />
                                                ) : (
                                                    <ErrorIcon className="text-red-500" />
                                                );
                                            }
                                            return <VerifiedUserIcon className="text-blue-500" />;
                                        } else if (activeStep === index) {
                                            return <BuildIcon className="text-blue-500" />;
                                        } else {
                                            return <BuildIcon className="text-gray-300" />;
                                        }
                                    }}
                                >
                                    {label}
                                </StepLabel>
                            </Step>
                        ))}
                    </Stepper>
                    <div className="mt-8">{getStepContent(activeStep)}</div>
                </div>
            </div>
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert
                    onClose={() => setSnackbar({ ...snackbar, open: false })}
                    severity={snackbar.severity}
                    className="w-full"
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </div>
    );
};

export default Home;