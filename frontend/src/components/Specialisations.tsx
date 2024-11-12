import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    DataGrid,
    GridColDef,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowParams, GridToolbar,
} from '@mui/x-data-grid';
import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import SpecialisationModal from './SpecialisationModal';
import { RootState, AppDispatch } from '../app/store';
import {
    fetchSpecialisations,
    deleteSpecialisation,
} from '../app/slices/specialisationSlice';
import { cycleMapping, Specialisation} from '../utils/Interfaces';
import { plPL } from '@mui/x-data-grid/locales';

const Specialisations: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const specialisations = useSelector((state: RootState) => state.specialisations.rows);
    const loading = useSelector((state: RootState) => state.specialisations.loading);
    const error = useSelector((state: RootState) => state.specialisations.error);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<number | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSpecialisation, setSelectedSpecialisation] = React.useState<Specialisation | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);

    useEffect(() => {
        dispatch(fetchSpecialisations());
    }, [dispatch]);

    const handleViewClick = (id: number) => () => {
        const specialisation = specialisations.find((spec) => spec.id === id);
        if (specialisation) {
            setSelectedSpecialisation(specialisation);
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
            dispatch(deleteSpecialisation(selectedRowId));
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedSpecialisation(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        { field: 'name', headerName: 'Nazwa', width: 200 },
        {
            field: 'cycle',
            headerName: 'Cykl',
            width: 150,
            valueGetter: (value) => cycleMapping[value] || value,
        },        { field: 'fieldOfStudyName', headerName: 'Kierunek', width: 150 },
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
            <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                Dodaj Specjalizację
            </Button>
            <div style={{ flexGrow: 1 }} />
            <GridToolbar />
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={specialisations}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć tę specjalizację?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <SpecialisationModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    specialisation={selectedSpecialisation}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Specialisations;
