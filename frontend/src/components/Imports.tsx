import React, {useState} from 'react';
import {
    Button,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
    Typography
} from '@mui/material';
import {useSnackbar} from 'notistack';
import { MuiFileInput } from 'mui-file-input';
import API_ENDPOINTS from '../app/urls.ts';

const Imports: React.FC = () => {
    const {enqueueSnackbar} = useSnackbar();
    const [importType, setImportType] = useState<string>('XML');
    const [file, setFile] = React.useState<File | null>(null)

    const handleChange = (newFile: File | null) => {
        setFile(newFile)
    };

    const uploadFile = async () => {
        try {
            let url: string;

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
            }

            const formData = new FormData();
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            formData.append('file', file);

            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            const res = await fetch(url, {
                method: 'POST',
                body: formData,
            });

            if (res.status === 200) {
                enqueueSnackbar('Import udany.', {variant: 'success'});

            } else {
                enqueueSnackbar('Nie udało zaimportować pliku.', {variant: 'error'});
            }

        } catch (e: any) {
            enqueueSnackbar(`Błąd podczas przesyłania pliku: ${e.message}`, {variant: 'error'});
        }
    };

    return (<section className="flex flex-col justify-around items-center content-center gap-4 p-4">
            <Typography variant="h4">Zaimportuj plik</Typography>

            <div className="flex gap-4 w-full max-w-md justify-center items-center flex-col">
                <div className="flex flex-row items-center">
                    <FormControl className="w-56">
                        <InputLabel id="importType-label">Typ importu</InputLabel>
                        <Select
                            labelId="importType-label"
                            value={importType}
                            label="Typ importu"
                            onChange={(event: SelectChangeEvent) => setImportType(event.target.value)}
                        >
                            <MenuItem value="XML">Plan .xml</MenuItem>
                            <MenuItem value="INNER_ID">Numery kart przydziału czynności .xlsx</MenuItem>
                            <MenuItem value="LOAD_SHEET">Obciążenia .xlsx</MenuItem>
                        </Select>
                    </FormControl>
                </div>
                <div className="flex flex-row gap-4">
                    <MuiFileInput
                        value={file}
                        onChange={handleChange}
                    />
                </div>
            </div>
            <Button variant="contained" color="primary" onClick={uploadFile} disabled={file === null}>
                {'Prześlij plik'}
            </Button>
        </section>
    );
};

export default Imports;
