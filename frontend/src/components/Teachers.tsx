import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    DataGrid, GridColDef, GridToolbarContainer,
    GridActionsCellItem, GridRowParams, GridToolbar,
} from '@mui/x-data-grid';
import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import { RootState, AppDispatch } from '../app/store';
import { plPL } from '@mui/x-data-grid/locales';
import { fetchTeachers, deleteTeacher } from '../app/slices/teacherSlice';
import TeacherModal from './TeacherModal';
import { degrees, Teacher } from '../utils/Interfaces';

const Teachers: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { rows: teachers, loading, error } = useSelector((state: RootState) => state.teachers);

    const [isDialogOpen, setDialogOpen] = useState(false);
    const [selectedRowId, setSelectedRowId] = useState<number | null>(null);
    const [isModalOpen, setModalOpen] = useState(false);
    const [selectedTeacher, setSelectedTeacher] = useState<Teacher | null>(null);
    const [isAdding, setIsAdding] = useState(false);

    useEffect(() => {
        dispatch(fetchTeachers());
    }, [dispatch]);

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
            dispatch(deleteTeacher(selectedRowId));
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedTeacher(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        { field: 'firstName', headerName: 'Imię', width: 150 },
        { field: 'lastName', headerName: 'Nazwisko', width: 150 },
        {
            field: 'degree',
            headerName: 'Stopień',
            width: 150,
            valueFormatter: (params) => degrees[params as keyof typeof degrees] || '',
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
            getActions: (params: GridRowParams) => [
                <GridActionsCellItem
                    icon={<VisibilityIcon />}
                    label="Szczegóły"
                    onClick={handleViewClick(params.id as number)}
                    color="inherit"
                />,
                <GridActionsCellItem
                    icon={<DeleteIcon />}
                    label="Usuń"
                    onClick={handleDeleteClick(params.id as number)}
                    color="inherit"
                />,
            ],
        },
    ];

    const TopToolbar = () => (
        <GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                Dodaj nauczyciela
            </Button>
            <GridToolbar />
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={teachers}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć tego nauczyciela?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <TeacherModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    teacher={selectedTeacher}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Teachers;
