import React, {useEffect, useState} from 'react';
import {
    Box,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Fade,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Typography,
} from '@mui/material';
import CheckIcon from '@mui/icons-material/Check';
import {useDispatch} from 'react-redux';
import {AppDispatch} from '../app/store';
import {BackendSpecialisation, cycleMapping, Semester} from '../utils/Interfaces';
import {green} from "@mui/material/colors";
import API_ENDPOINTS from '../app/urls';
import {SelectChangeEvent} from '@mui/material/Select';
import {addSemester, fetchSemesters, updateSemester} from "../app/slices/semesterSlice.ts";
import {useSnackbar} from "notistack";
import ActionButton from "../utils/ActionButton.tsx";
import SaveIcon from "@mui/icons-material/Save";
import ClearIcon from "@mui/icons-material/Clear";
import {fetchWithAuth} from "../app/fetchWithAuth.ts";

interface SemesterModalProps {
    open: boolean;
    onClose: () => void;
    semester: Semester | null;
    isAdding: boolean;
}

const SemesterModal: React.FC<SemesterModalProps> = ({open, onClose, semester, isAdding}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();
    const [specialisations, setSpecialisations] = useState<BackendSpecialisation[]>([]);
    const [formData, setFormData] = useState({
        id: semester?.id || '',
        number: semester?.number || '',
        specialisationId: semester?.specialisationId?.toString() || '',
        specialisationRepresentation: semester?.specialisationRepresentation || '',
        groupCount: semester?.groupCount || 0,
        typ: semester?.typ || '',
    });
    const [loading, setLoading] = useState<boolean>(false);
    const [success, setSuccess] = useState<boolean>(false);

    useEffect(() => {
        if (isAdding) {
            fetchWithAuth(API_ENDPOINTS.SPECIALISATIONS)
                .then(res => res.json())
                .then((data: BackendSpecialisation[]) => setSpecialisations(data))
                .catch(err => {
                    enqueueSnackbar(`Wystąpił błąd przy pobieraniu specjalizacji: ${err}`, {variant: 'error'});
                });
        }
    }, [enqueueSnackbar, isAdding]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const value = e.target.value;
        setFormData((prev) => ({
            ...prev, number: value,
        }));
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const value = event.target.value;
        setFormData((prev) => ({
            ...prev, groupCount: Number(value),
        }));
    };

    const handleSpecialisationChange = (event: SelectChangeEvent) => {
        const selectedSpecialisationId = event.target.value as string;
        const selectedSpecialisation = specialisations.find(specialisation => specialisation!.specialisationId!.toString() === selectedSpecialisationId);
        setFormData({
            ...formData,
            specialisationId: selectedSpecialisationId,
            specialisationRepresentation: selectedSpecialisation?.name || selectedSpecialisationId,
        });
    };

    const handleTypChange = (event: SelectChangeEvent) => {
      setFormData({
          ...formData,
          typ:event.target.value,
      })
    };

    const handleSubmit = async () => {
        if (!formData.specialisationId) {
            enqueueSnackbar("Proszę wypełnić wszystkie pola", {variant: 'warning'});
            return;
        }

        setLoading(true);

        const semesterData = {
            number: formData.number, typ: formData.typ, groupCount: formData.groupCount, specialisation: {
                specialisationId: Number(formData.specialisationId),
            }, ...(isAdding ? {} : {semesterId: Number(formData.id)}),
        };

        try {
            console.log(semesterData);
            const action = isAdding ? addSemester : updateSemester;
            await dispatch(action(semesterData)).unwrap();
            await dispatch(fetchSemesters());
            setSuccess(true);
            setTimeout(() => {
                setLoading(false);
                setSuccess(false);
                onClose();
            }, 1000);
        } catch (error: any) {
            enqueueSnackbar(`Wystąpił błąd przy ${isAdding ? 'dodawaniu' : 'aktualizacji'} rekordu: ${error.message || error}`, {variant: 'error'});
            setLoading(false);
        }
    };

    return (<Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
            <Fade in={open} timeout={500}>
                <Box sx={{position: 'relative', minHeight: '200px', padding: 4}}>
                    {!success ? (<>
                            <DialogTitle>{isAdding ? 'Dodaj semestr' : 'Szczegóły semestru'}</DialogTitle>
                            <DialogContent>
                                {isAdding ? (<FormControl fullWidth margin="normal" disabled={loading}>
                                        <InputLabel id="semester-label">Specjalizacja</InputLabel>
                                        <Select
                                            labelId="semester-label"
                                            value={formData.specialisationId}
                                            onChange={handleSpecialisationChange}
                                            label="Specjalizacja"
                                            variant="outlined"
                                        >
                                            {specialisations.map((specialisation) => (
                                                <MenuItem key={specialisation.specialisationId}
                                                          value={specialisation!.specialisationId!.toString()}>
                                                    {`${specialisation.name} (${specialisation.fieldOfStudy.name} - ${cycleMapping[specialisation.cycle] || specialisation.cycle})`}
                                                </MenuItem>))}
                                        </Select>
                                    </FormControl>) : (<TextField
                                        margin="normal"
                                        label="Specjalizacja"
                                        value={formData.specialisationRepresentation}
                                        fullWidth
                                        disabled
                                    />)}
                                <TextField
                                    margin="normal"
                                    label="Oznaczenie"
                                    name="number"
                                    value={formData.number}
                                    onChange={handleInputChange}
                                    fullWidth
                                    disabled={loading}
                                />
                                <FormControl fullWidth margin="normal" disabled={loading}>
                                    <InputLabel id="semester-typ-label">Typ</InputLabel>
                                    <Select
                                        labelId="semester-typ-label"
                                        value={formData.typ}
                                        onChange={handleTypChange}
                                        label="Typ"
                                        variant="outlined"
                                    >
                                        <MenuItem key="letni" value="letni">LETNI</MenuItem>
                                        <MenuItem key="zimowy" value="zimowy">ZIMOWY</MenuItem>
                                    </Select>
                                </FormControl>
                                <TextField
                                    margin="normal"
                                    label="Liczba grub laboratoryjnych"
                                    type="number"
                                    name="groupCount"
                                    value={formData.groupCount}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                />
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
                        </>) : (<Fade in={success} timeout={1000}>
                            <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                                <CheckIcon sx={{fontSize: 60, color: green[500], mb: 2}}/>
                                <Typography variant="h6" color="green">
                                    Dodano!
                                </Typography>
                            </Box>
                        </Fade>)}
                </Box>
            </Fade>
        </Dialog>);
};

export default SemesterModal;
