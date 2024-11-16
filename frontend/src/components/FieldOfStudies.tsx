import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
    DataGrid,
    GridActionsCellItem,
    GridColDef,
    GridRowId,
    GridRowModel,
    GridRowModes,
    GridRowModesModel,
    GridRowParams,
    GridToolbar,
    GridToolbarContainer,
} from '@mui/x-data-grid';
import {Button} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {
    setRowModesModel,
    setSelectedRow,
    clearSelectedRow,
    addNewFOS,
    removeNewFOS,
    fetchFOS,
    addFOS,
    updateFOS,
    deleteFOS,
} from '../app/slices/fieldOfStudySlice.ts';
import {FieldOfStudy} from '../utils/Interfaces.ts';
import {plPL} from "@mui/x-data-grid/locales";
import {useSnackbar} from "notistack";

const FieldOfStudies: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.fields.rows);
    const rowModesModel = useSelector((state: RootState) => state.fields.rowModesModel);
    const selectedRowName = useSelector((state: RootState) => state.fields.selectedRowName);
    const selectedRowId = useSelector((state: RootState) => state.fields.selectedRowId);
    const loading = useSelector((state: RootState) => state.fields.loading);
    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const { enqueueSnackbar } = useSnackbar();

    const [rowIdCounter, setRowIdCounter] = React.useState(-1);

    useEffect(() => {
        dispatch(fetchFOS()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania kierunków studiów: ${error.message}`, { variant: 'error' });
        });
    }, [dispatch, enqueueSnackbar]);


    const handleEditClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } }));
    };

    const handleSaveClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } }));
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        const rowToDelete = rows.find((row) => row.id === id);
        if (rowToDelete) {
            dispatch(setSelectedRow({ id, name: rowToDelete.name }));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteFOS(selectedRowId))
                .unwrap()
                .then(() => {
                    enqueueSnackbar('Kierunek został pomyślnie usunięty', { variant: 'success' });
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas usuwania kierunku: ${error.message}`, { variant: 'error' });
                });
        }
        setDialogOpen(false);
        dispatch(clearSelectedRow());
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.View, ignoreModifications: true },
            })
        );

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow?.isNew) {
            dispatch(removeNewFOS(id));
        }
    };

    const processRowUpdate = async (newRow: GridRowModel) => {
        const updatedRow: FieldOfStudy = {
            id: newRow.id,
            name: newRow.name || '',
            isNew: newRow.isNew || false,
        };

        if (updatedRow.isNew) {
            const resultAction = await dispatch(addFOS(updatedRow));
            if (addFOS.fulfilled.match(resultAction)) {
                const { fos } = resultAction.payload;
                return fos;
            } else {
                enqueueSnackbar(`Wystąpił błąd przy dodawaniu kierunku`, { variant: 'error' });
            }
        } else {
            const resultAction = await dispatch(updateFOS(updatedRow));
            if (updateFOS.fulfilled.match(resultAction)) {
                return resultAction.payload;
            } else {
                enqueueSnackbar(`Wystąpił błąd przy aktualizacji kierunku`, { variant: 'error' });
            }
        }
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns: GridColDef[] = [
        { field: 'name', headerName: 'Kod', width: 150, editable: true },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
            width: 100,
            getActions: (params: GridRowParams) => {
                const id = params.id;
                const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

                if (isInEditMode) {
                    return [
                        <GridActionsCellItem
                            key="save"
                            icon={<SaveIcon />}
                            label="Zapisz"
                            sx={{ color: 'primary.main' }}
                            onClick={handleSaveClick(id)}
                        />,
                        <GridActionsCellItem
                            key="cancel"
                            icon={<CancelIcon />}
                            label="Anuluj"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                        />,
                    ];
                }

                return [
                    <GridActionsCellItem
                        key="edit"
                        icon={<EditIcon />}
                        label="Edytuj"
                        onClick={handleEditClick(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        key="delete"
                        icon={<DeleteIcon />}
                        label="Usuń"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                    />,
                ];
            },
        },
    ];

    const handleAddClick = () => {
        const id = rowIdCounter;
        setRowIdCounter((prev) => prev - 1);
        const newFOS: FieldOfStudy = {
            id,
            name: '',
            isNew: true,
        };
        dispatch(addNewFOS(newFOS));
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.Edit, fieldToFocus: 'name' },
            })
        );
    };

    const TopToolbar = () => {
        return (
            <GridToolbarContainer>
                <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                    Dodaj kierunek studiów
                </Button>
                <GridToolbar />
            </GridToolbarContainer>
        );
    };

    return (
        <>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                processRowUpdate={processRowUpdate}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content={`Czy na pewno chcesz usunąć kierunek studiów? ${selectedRowName}?`}
                action="Potwierdź"
            />
        </>
    );
};

export default FieldOfStudies;
