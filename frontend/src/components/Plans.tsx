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
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {
    fetchPlans,
    clearSelectedRow,
    removeNewPlan,
    setRowModesModel,
    setSelectedRow, deletePlan, updatePlan,
} from '../app/slices/planSlice';
import {Plan} from '../utils/Interfaces.ts';
import {plPL} from "@mui/x-data-grid/locales";
import {useSnackbar} from "notistack";

const Plans: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.plans.rows);
    const rowModesModel = useSelector((state: RootState) => state.plans.rowModesModel);
    const selectedRowCode = useSelector((state: RootState) => state.plans.selectedRowCode);
    const selectedRowId = useSelector((state: RootState) => state.plans.selectedRowId);
    const loading = useSelector((state: RootState) => state.plans.loading);
    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        dispatch(fetchPlans()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania planów: ${error.message}`, {variant: 'error'});
        });
    }, [dispatch, enqueueSnackbar]);


    const handleEditClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({...rowModesModel, [id]: {mode: GridRowModes.Edit}}));
    };

    const handleSaveClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({...rowModesModel, [id]: {mode: GridRowModes.View}}));
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        const rowToDelete = rows.find((row) => row.planId === id);
        if (rowToDelete) {
            dispatch(setSelectedRow({id, name: rowToDelete.name}));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deletePlan(selectedRowId))
                .unwrap()
                .then(() => {
                    enqueueSnackbar('Plan został pomyślnie usunięty', {variant: 'success'});
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas usuwania planu: ${error.message}`, {variant: 'error'});
                });
        }
        setDialogOpen(false);
        dispatch(clearSelectedRow());
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({
            ...rowModesModel, [id]: {mode: GridRowModes.View, ignoreModifications: true},
        }));

        const editedRow = rows.find((row) => row.planId === id);
        if (editedRow?.isNew) {
            dispatch(removeNewPlan(id));
        }
    };

    const processRowUpdate = async (newRow: GridRowModel) => {
        const updatedRow: Plan = {
            published: newRow.published,
            creationDate:  newRow.creationDate,
            planId: newRow.planId, name: newRow.name || '', isNew: newRow.isNew || false
        };
        if (!updatedRow.isNew) {
            const resultAction = await dispatch(updatePlan(updatedRow));
            if (updatePlan.fulfilled.match(resultAction)) {
                dispatch(fetchPlans()).unwrap().catch((error) => {
                    enqueueSnackbar(`Błąd podczas pobierania planów: ${error.message}`, {variant: 'error'});
                });
                return resultAction.payload;
            } else {
                enqueueSnackbar(`Wystąpił błąd przy aktualizacji planu`, {variant: 'error'});
            }
        }
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns: GridColDef[] = [{field: 'name', headerName: 'Nazwa', width: 150, editable: true},
        {field: 'creationDate', headerName: 'Data utworzenia', width: 200, editable: false, valueGetter: (value) =>  new Date(value).toLocaleString()},
        {field: 'published',type: 'boolean', headerName: 'Opublikowany', width: 150, editable: true},{
        field: 'actions', type: 'actions', headerName: 'Akcje', width: 100, getActions: (params: GridRowParams) => {
            const id = params.id;
            const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

            if (isInEditMode) {
                return [<GridActionsCellItem
                    key="save"
                    icon={<SaveIcon/>}
                    label="Zapisz"
                    sx={{color: 'primary.main'}}
                    onClick={handleSaveClick(id)}
                />, <GridActionsCellItem
                    key="cancel"
                    icon={<CancelIcon/>}
                    label="Anuluj"
                    onClick={handleCancelClick(id)}
                    color="inherit"
                />,];
            }

            return [<GridActionsCellItem
                key="edit"
                icon={<EditIcon/>}
                label="Edytuj"
                onClick={handleEditClick(id)}
                color="inherit"
            />, <GridActionsCellItem
                key="delete"
                icon={<DeleteIcon/>}
                label="Usuń"
                onClick={handleDeleteClick(id)}
                color="inherit"
            />,];
        },
    },];

    const TopToolbar = () => {
        return (<GridToolbarContainer>
                <GridToolbar/>
            </GridToolbarContainer>);
    };

    return (<>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                processRowUpdate={processRowUpdate}
                slots={{toolbar: TopToolbar}}
                getRowId={(row) => row.planId}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content={`Czy na pewno chcesz usunąć plan ${selectedRowCode}?`}
                action="Potwierdź"
            />
        </>);
};

export default Plans;
