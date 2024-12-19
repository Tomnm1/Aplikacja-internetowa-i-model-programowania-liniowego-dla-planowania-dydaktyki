import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
    DataGrid, GridActionsCellItem, GridColDef, GridRowParams, GridToolbar, GridToolbarContainer,
} from '@mui/x-data-grid';
import {Button, Tooltip} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {
    BackendSemester,
    Cycle,
    cycleMapping,
    Language,
    languageMapping,
    Subject
} from '../utils/Interfaces';
import {plPL} from '@mui/x-data-grid/locales';
import {deleteSubject, fetchSubject} from "../app/slices/subjectSlice.ts";
import SubjectModal from "./SubjectModal.tsx";
import {useSnackbar} from "notistack";
import ErrorIcon from '@mui/icons-material/Error';

const Subjects: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const {rows: subjects, loading} = useSelector((state: RootState) => state.subjects);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<number | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSubject, setSelectedSubject] = React.useState<Subject | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        dispatch(fetchSubject()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania przedmiotów: ${error.message}`, {variant: 'error'});
        });
    }, [dispatch, enqueueSnackbar]);


    const handleViewClick = (id: number) => () => {
        const subject = subjects.find((s) => s.SubjectId === id);
        if (subject) {
            setSelectedSubject(subject);
            setIsAdding(false);
            setModalOpen(true);
        }
    };

    const handleDeleteClick = (id: number) => () => {
        setSelectedRowId(id);
        setDialogOpen(true);
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteSubject(selectedRowId))
                .unwrap()
                .then(() => {
                    enqueueSnackbar('Przedmiot został pomyślnie usunięty', {variant: 'success'});
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas usuwania przedmiotu: ${error.message}`, {variant: 'error'});
                });
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };


    const handleAddClick = () => {
        setSelectedSubject(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const errorMsg = (c:boolean, g:boolean, t:boolean) => {
        if(!c) return 'Brak przypisanych SAL'
        if(!g) return 'Brak przypisanych GRUP'
        if(!t) return 'Błędne przypisanie PROWADZĄCYCH'
    }

    const columns: GridColDef[] = [{ field: '', headerName: '', width: 50, renderCell: (params) => {
            return ((!params.row.checkClassrooms || !params.row.checkGroups || !params.row.checkTeachers) &&
                <Tooltip title={errorMsg(params.row.checkClassrooms,params.row.checkGroups,params.row.checkTeachers)}>
                    <ErrorIcon color="error"/>
                </Tooltip>
            );
        }, valueGetter: (_value, row) => {{
            return (!row.checkClassrooms || !row.checkGroups || !row.checkTeachers) ? 'a' : 'b';
        }},
        filterable: false
        }, {
        field: 'name', headerName: 'Nazwa', width: 300, valueGetter: (_value, row) => row?.name || "NIE MA",
    }, {
        field: 'semester', headerName: 'Semestr', width: 100, valueGetter: (value: BackendSemester) => value.number,
    }, {
        field: 'specialisation',
        headerName: 'Specjalizacja',
        width: 100,
        valueGetter: (_value, row) => row.semester.specialisation?.name || '',
    }, {
        field: 'fieldOfStudy',
        headerName: 'Kierunek',
        width: 150,
        valueGetter: (_value, row) => row.semester.specialisation?.fieldOfStudy?.name || '',

    }, {
        field: 'Cicle',
        headerName: 'Cykl',
        width: 100,
        valueGetter: (_value, row: Subject) => cycleMapping[row.semester.specialisation?.cycle as Cycle] || row.semester.specialisation?.cycle,
    }, {
        field: 'language',
        headerName: 'Język',
        width: 100,
        valueGetter: (params) => languageMapping[params as Language],
    }, {
        field: 'exam', headerName: 'Egzamin', type: 'boolean', width: 100,
    }, {
        field: 'mandatory', headerName: 'Obowiązkowy', type: 'boolean', width: 100,
    }, {
        field: 'planned', headerName: 'Planowany', type: 'boolean', width: 100,
    }, {
        field: 'actions',
        type: 'actions',
        headerName: 'Akcje',
        getActions: (params: GridRowParams) => [<GridActionsCellItem
            icon={<VisibilityIcon/>}
            label="Szczegóły"
            onClick={handleViewClick(params.id as number)}
            color="inherit"
        />, <GridActionsCellItem
            icon={<DeleteIcon/>}
            label="Usuń"
            onClick={handleDeleteClick(params.id as number)}
            color="inherit"
        />,],
    },];

    const TopToolbar = () => (<GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon/>} onClick={handleAddClick}>
                Dodaj przedmiot
            </Button>
            <div style={{flexGrow: 1}}/>
            <GridToolbar/>
        </GridToolbarContainer>);

    return (<>
        <DataGrid
            rows={subjects}
            columns={columns}
            loading={loading}
            localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
            slots={{toolbar: TopToolbar}}
            getRowId={(row) => row.SubjectId}
            getRowClassName={params => {
                return (!params.row.checkClassrooms || !params.row.checkGroups || !params.row.checkTeachers) ? 'highlight-row' : '';
            }}
        />
        <ConfirmationDialog
            open={isDialogOpen}
            onClose={handleDialogClose}
            title="Potwierdzenie"
            content="Czy na pewno chcesz usunąć ten przedmiot?"
            action="Potwierdź"
        />
        {isModalOpen && (<SubjectModal
            open={isModalOpen}
            onClose={() => setModalOpen(false)}
            subject={selectedSubject}
            isAdding={isAdding}
        />)}
    </>);
};

export default Subjects;