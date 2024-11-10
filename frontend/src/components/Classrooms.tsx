import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    DataGrid,
    GridColDef,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowId,
    GridRowParams, GridToolbar,
} from '@mui/x-data-grid';
import {
    Button,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import ClassroomModal from './ClassroomModal';
import { RootState, AppDispatch } from '../app/store';
import {
    fetchClassrooms,
    deleteClassroom,
} from '../app/slices/classroomSlice';
import { Classroom } from '../utils/Interfaces.ts';
import { plPL } from '@mui/x-data-grid/locales';


const Classrooms: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const classrooms = useSelector((state: RootState) => state.classrooms.rows);
    const loading = useSelector((state: RootState) => state.classrooms.loading);
    const error = useSelector((state: RootState) => state.classrooms.error);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<GridRowId | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedClassroom, setSelectedClassroom] = React.useState<Classroom | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);

    useEffect(() => {
        dispatch(fetchClassrooms());
    }, [dispatch]);

    const handleViewClick = (id: GridRowId) => () => {
        const classroom = classrooms.find((cls) => cls.id === id);
        if (classroom) {
            setSelectedClassroom(classroom);
            setIsAdding(false);
            setModalOpen(true);
        }
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        setSelectedRowId(id);
        setDialogOpen(true);
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            if (typeof selectedRowId === "number") {
                dispatch(deleteClassroom(selectedRowId));
            }
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedClassroom(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        { field: 'code', headerName: 'Kod', width: 100 },
        { field: 'buildingCode', headerName: 'Budynek', width: 100 },
        // { field: 'floor', headerName: 'Piętro', width: 100 },
        { field: 'capacity', headerName: 'Pojemność', width: 100 },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
            getActions: (params: GridRowParams) => [
                <GridActionsCellItem
                    icon={<VisibilityIcon />}
                    label="Szczegóły"
                    onClick={handleViewClick(params.id)}
                    color="inherit"
                />,
                <GridActionsCellItem
                    icon={<DeleteIcon />}
                    label="Usuń"
                    onClick={handleDeleteClick(params.id)}
                    color="inherit"
                />,
            ],
        },
    ];
    const TopToolbar = () => (
        <GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                Dodaj salę
            </Button>
            <GridToolbar/>
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={classrooms}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć tę salę?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <ClassroomModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    classroom={selectedClassroom}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Classrooms;
