import React, { useEffect } from 'react';
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
import {cycleMapping, Semester} from '../utils/Interfaces';
import { plPL } from '@mui/x-data-grid/locales';
import {deleteSemester, fetchSemesters} from "../app/slices/semesterSlice.ts";
import SemesterModal from "./SemesterModal.tsx";

const Semesters: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { rows: semesters, loading, error } = useSelector((state: RootState) => state.semesters);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<number | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSemester, setSelectedSemester] = React.useState<Semester | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);

    useEffect(() => {
        dispatch(fetchSemesters());
    }, [dispatch]);

    const handleViewClick = (id: number) => () => {
        const semester = semesters.find((s) => s.id === id);
        if (semester) {
            setSelectedSemester(semester);
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
            dispatch(deleteSemester(selectedRowId));
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedSemester(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        {
            field: 'number',
            headerName: 'Oznaczenie',
            width: 150,
        },
        {
            field: 'specialisationRepresentation',
            headerName: 'Specjalizacja',
            width: 100,
        },
        {
            field: 'fieldOfStudyName',
            headerName: 'Kierunek',
            width: 150,
        },
        {
            field: 'cycle',
            headerName: 'Cykl',
            width: 100,
            valueGetter: (value) => cycleMapping[value] || value,
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
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
            <Button color="primary" startIcon={<AddIcon/>} onClick={handleAddClick}>
                Dodaj semestr
            </Button>
            <div style={{flexGrow: 1}}/>
            <GridToolbar/>
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={semesters}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć ten semestr?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <SemesterModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    semester={selectedSemester}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};
export default Semesters;
