import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
    DataGrid, GridActionsCellItem, GridColDef, GridRowParams, GridToolbar, GridToolbarContainer,
} from '@mui/x-data-grid';
import {Button} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {plPL} from '@mui/x-data-grid/locales';
import {deleteTeacher, fetchTeachers, updateTeacherEmail} from '../app/slices/teacherSlice';
import TeacherModal from './TeacherModal';
import {degrees, Teacher} from '../utils/Interfaces';
import {useSnackbar} from "notistack";

const Teachers: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const {rows: teachers, loading} = useSelector((state: RootState) => state.teachers);

    const [isDialogOpen, setDialogOpen] = useState(false);
    const [selectedRowId, setSelectedRowId] = useState<number | null>(null);
    const [isModalOpen, setModalOpen] = useState(false);
    const [selectedTeacher, setSelectedTeacher] = useState<Teacher | null>(null);
    const [isAdding, setIsAdding] = useState(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        dispatch(fetchTeachers()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania przedmiotów: ${error.message}`, {variant: 'error'});
        });
    }, [dispatch, enqueueSnackbar]);

    const handleViewClick = (id: number) => () => {
        const teacher = teachers.find((t) => t.id === id);
        if (teacher) {
            setSelectedTeacher(teacher);
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
            dispatch(deleteTeacher(selectedRowId))
                .unwrap()
                .then(() => {
                    enqueueSnackbar('Pracownik został pomyślnie usunięty', {variant: 'success'});
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas usuwania pracownika: ${error.message}`, {variant: 'error'});
                });
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedTeacher(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const handleEmailEdit = (newRow:Teacher, oldRow:Teacher) => {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if(newRow.email && !emailRegex.test(newRow.email)){
            enqueueSnackbar(`Nie poprawny adres email: ${newRow.email}`, {variant: 'error'});
            return oldRow;
        }
        if(newRow.email  !== oldRow.email){
            dispatch(updateTeacherEmail(newRow)).unwrap()
                .then(() => {
                    enqueueSnackbar('Mail został pomyślnie zedytowany', {variant: 'success'});
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas edytowania maila pracownika: ${error.message}`, {variant: 'error'});
                });
        }
        return newRow;
    };

    const columns: GridColDef[] = [{field: 'firstName', headerName: 'Imię', width: 150}, {
        field: 'lastName',
        headerName: 'Nazwisko',
        width: 150
    }, {
        field: 'degree',
        headerName: 'Stopień',
        width: 150,
        valueFormatter: (params) => degrees[params as keyof typeof degrees] || '',
    }, {
        field: 'email',
        headerName: 'Mail',
        width: 200,
        editable: true,
    },
        // {
        //     field: 'subjectTypesList',
        //     headerName: 'Typy Przedmiotów',
        //     width: 250,
        //     valueFormatter: (params) =>
        //         params.value
        //             ? params.value.join(', ')
        //             : '',
        // },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
            width: 100,
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
                Dodaj nauczyciela
            </Button>
            <GridToolbar/>
        </GridToolbarContainer>);

    return (<>
            <DataGrid
                rows={teachers}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{toolbar: TopToolbar}}
                processRowUpdate={handleEmailEdit}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć tego nauczyciela?"
                action="Potwierdź"
            />
            {isModalOpen && (<TeacherModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    teacher={selectedTeacher}
                    isAdding={isAdding}
                />)}
        </>);
};

export default Teachers;
