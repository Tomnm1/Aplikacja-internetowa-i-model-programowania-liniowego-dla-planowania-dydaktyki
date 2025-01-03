import React, {useState} from 'react';
import {
    Button, FormControl, FormHelperText, InputLabel, MenuItem, Select, SelectChangeEvent, Typography
} from '@mui/material';
import {useSnackbar} from 'notistack';
import {MuiFileInput} from 'mui-file-input';
import API_ENDPOINTS from '../app/urls.ts';
import CloseIcon from '@mui/icons-material/Close';

const Import: React.FC = () => {
    const {enqueueSnackbar} = useSnackbar();
    const [importType, setImportType] = useState<string>('XML');
    const [file, setFile] = React.useState<File | null>(null)
    const acceptedTypes = ['.xml', '.xlsx']

    const handleChange = (newFile: File | null) => {
        setFile(newFile)
    };

    const uploadFile = async () => {
        try {
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            if (!file) return; //to rozwiązuje (1) dlaczego - ewentualnie jakaś obsluga błędu / toast
            
            let url: string; //wstawiamy tu i powinno to naprawić problem z (3) dlaczego

            
            const fileExtension: string = file.name.substring(file.name.lastIndexOf('.')).toLocaleLowerCase();
            const isSupported: boolean = acceptedTypes.includes(fileExtension);

            if (!isSupported) {
                throw new Error("Niepoprawny typ pliku!");
            }

            //let url: string; -> to usuwamy

            switch (importType) {
                case 'XML':
                    url = API_ENDPOINTS.IMPORT_XML;
                    break;
                case 'INNER_ID':
                    url = API_ENDPOINTS.IMPORT_INNER_ID;
                    break;
                case 'LOAD_SHEET':
                    url = API_ENDPOINTS.IMPORT_LOAD_SHEET;
                    break;
                default:
                    console.error('Import type not implemented');
                    enqueueSnackbar('Niepoprawny typ importu!', {variant: 'error'});
                    return; //to naprawia (2) dlaczego
            }

            const formData = new FormData();
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            formData.append('file', file);

            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            const res = await fetch(url, {
                method: 'POST', body: formData,
            });

            setFile(null);
            if (res.status === 200) {
                enqueueSnackbar('Import udany.', {variant: 'success'});
            } else {
                enqueueSnackbar('Nie udało się zaimportować pliku.', {variant: 'error'});
            }

        } catch (e: any) {
            enqueueSnackbar(`Błąd podczas przesyłania pliku: ${e.message}`, {variant: 'error'});
        }
    };

    return (<section className="flex flex-col justify-around items-center content-center gap-4 p-4">
        <Typography variant="h4">Zaimportuj plik</Typography>
        <div className="flex gap-4 w-full max-w-md justify-center items-center flex-col">
            <div className="flex flex-row items-center">
                <FormControl className="w-96">
                    <InputLabel id="importType-label">Typ importu</InputLabel>
                    <Select
                        labelId="importType-label"
                        value={importType}
                        label="Typ importu"
                        onChange={(event: SelectChangeEvent) => setImportType(event.target.value)}
                    >
                        <MenuItem value="XML">1. Plan (.xml)</MenuItem>
                        <MenuItem value="INNER_ID">2. Numery kart przydziału czynności (.xlsx)</MenuItem>
                        <MenuItem value="LOAD_SHEET">3. Obciążenia (.xlsx)</MenuItem>
                    </Select>
                    <FormHelperText id="import-helper-text">Należy wykonywać importy w kolejności, w przeciwnym
                        wypadku może dojść do błędu.
                        Wielokrotny import tego samego pliku nie spowoduje duplikacji danych.</FormHelperText>
                </FormControl>
            </div>
            <div className="flex flex-row items-center">
                <FormControl className="w-96">
                    <MuiFileInput
                        className="overflow-clip text-wrap"
                        placeholder={"Wybierz plik"}
                        value={file}
                        onChange={handleChange}
                        inputProps={{accept: acceptedTypes}}
                        getInputText={(file) => file ? file.name : ''}
                        clearIconButtonProps={{
                            title: "Remove", children: <CloseIcon fontSize="medium"/>
                        }}
                    />
                </FormControl>
            </div>
        </div>
        <Button variant="contained" color="primary" onClick={uploadFile} disabled={file === null}>
            {'Prześlij plik'}
        </Button>
    </section>);
};

export default Import;
