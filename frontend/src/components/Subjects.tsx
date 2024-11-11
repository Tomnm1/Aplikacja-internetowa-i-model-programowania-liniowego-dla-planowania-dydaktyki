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
import {Language, languageMapping, Subject} from '../utils/Interfaces';
import { plPL } from '@mui/x-data-grid/locales';
import { deleteSubject, fetchSubject } from "../app/slices/subjectSlice.ts";
import SubjectModal from "./SubjectModal.tsx";

const Subjects: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const { rows: subjects, loading, error } = useSelector((state: RootState) => state.subjects);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<number | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSubject, setSelectedSubject] = React.useState<Subject | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);

    useEffect(() => {
        dispatch(fetchSubject());
    }, [dispatch]);

    const handleViewClick = (id: number) => () => {
        const subject = subjects.find((s) => s.subject_id === id);
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
            dispatch(deleteSubject(selectedRowId));
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedSubject(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        {
            field: 'name',
            headerName: 'Nazwa',
            width: 150,
        },
        {
            field:  'language',
            headerName: 'Język',
            width: 150,
            valueGetter: (params) => languageMapping[params as Language],
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
            <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                Dodaj slot dnia
            </Button>
            <GridToolbar />
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={subjects}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć ten slot dnia?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <SubjectModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    subject={selectedSubject}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Subjects;
