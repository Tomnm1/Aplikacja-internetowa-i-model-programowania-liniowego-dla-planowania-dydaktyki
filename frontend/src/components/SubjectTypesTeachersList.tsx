import React, {useState} from 'react';
import {IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import {Delete, Edit} from '@mui/icons-material';
import {BackendSubjectType, teacherListDTO} from '../utils/Interfaces';
import {useSnackbar} from 'notistack';
import AddIcon from "@mui/icons-material/Add";
import SubjectTypesTeachersModal from "./SubjectTypesTeachersModal.tsx";

interface SubjectTypesTeachersListProps {
    teachersList: teacherListDTO[];
    typeData: BackendSubjectType | null;
    maxHours: number;
    setSubjectTypes: React.Dispatch<React.SetStateAction<BackendSubjectType[]>>;
    loading: boolean;
}

const SubjectTypesTeachersList: React.FC<SubjectTypesTeachersListProps> = ({
                                                                               teachersList,
                                                                               typeData,
                                                                               maxHours,
                                                                               setSubjectTypes,
                                                                               loading
                                                                           }) => {
    const {enqueueSnackbar} = useSnackbar();
    const [isAdding, setIsAdding] = useState(false);
    const [openTeachersModal, setOpenTeachersModal] = useState(false);
    const [hours, setHours] = useState(() => {
        return maxHours - teachersList.reduce((sum, t) => {
            return sum + t.numHours;
        }, 0);
    });
    const [currentType, setCurrentType] = useState<teacherListDTO | null>(null);

    const handleAdd = () => {
        setCurrentType(null);
        setIsAdding(true);
        setOpenTeachersModal(true);
    };

    const handleEdit = (type: teacherListDTO) => {
        setCurrentType(type);
        setIsAdding(false);
        setOpenTeachersModal(true);
    };

    const handleDelete = async (typeData: teacherListDTO) => {
        setSubjectTypes(prev => prev.map(st => {
            if (st.subjectTypeId === typeData.subjectTypeId || st.frontId === typeData.frontId) {
                return {...st, teachersList: st.teachersList.filter(t => t.teacherId !== typeData.teacherId)}
            } else {
                return st;
            }
        }));
        setHours(prev => prev + typeData.numHours);
        enqueueSnackbar('Prowadzący usunięty!', {variant: 'success'});
    };

    const handleSave = (typeData: teacherListDTO) => {
        if (!isAdding) {
            setSubjectTypes(prev => prev.map(st => {
                if (st.subjectTypeId === typeData.subjectTypeId || st.frontId === typeData.frontId) {
                    return {
                        ...st,
                        teachersList: st.teachersList.map(t => t.teacherId === typeData.teacherId ? typeData : t)
                    };
                } else {
                    return st;
                }
            }));
            enqueueSnackbar('Prowadzący zaktualizowany!', {variant: 'success'});
        } else {
            setSubjectTypes(prev => prev.map(st => {
                if (st.subjectTypeId === typeData.subjectTypeId || st.frontId === typeData.frontId) {
                    return {...st, teachersList: [...st.teachersList, typeData]};
                } else {
                    return st;
                }
            }));
            enqueueSnackbar('Prowadzący dodany!', {variant: 'success'});
        }

        setHours(maxHours - teachersList.reduce((sum, t) => {
            return sum + t.numHours;
        }, 0));
        setOpenTeachersModal(false);
    };

    return (<>
            <TableRow style={{backgroundColor: '#eeeeee'}}>
                <TableCell colSpan={4}>
                    {hours !== 0 && <div className={"flex justify-center my-2 text-red-600 font-bold"}>Do
                        rozdysponowania: {hours}h</div>}
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Imię</TableCell>
                                    <TableCell>Nazwisko</TableCell>
                                    <TableCell>Liczba Godzin</TableCell>
                                    <TableCell>Akcje</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {teachersList.map((type) => (<TableRow key={type.id}>
                                        <TableCell>{type.teacherFirstName}</TableCell>
                                        <TableCell>{type.teacherLastName}</TableCell>
                                        <TableCell>{type.numHours}</TableCell>
                                        <TableCell>
                                            <div className={"flex"}>
                                                <IconButton onClick={() => handleEdit(type)} disabled={loading}>
                                                    <Edit/>
                                                </IconButton>
                                                <IconButton onClick={() => handleDelete(type)}
                                                            disabled={loading}>
                                                    <Delete/>
                                                </IconButton>
                                            </div>
                                        </TableCell>
                                    </TableRow>))}
                                {teachersList.length === 0 && (<TableRow>
                                        <TableCell colSpan={4} align="center">
                                            Brak przypisanych prowadzących.
                                        </TableCell>
                                    </TableRow>)}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <div className={"flex justify-center -mt-5"}>
                        <div className={"bg-white"}>
                            <IconButton onClick={handleAdd} disabled={loading}>
                                <AddIcon/>
                            </IconButton>
                        </div>
                    </div>
                </TableCell>
            </TableRow>

            {openTeachersModal && (<SubjectTypesTeachersModal
                    open={openTeachersModal}
                    onClose={() => setOpenTeachersModal(false)}
                    typeData={currentType}
                    onSave={handleSave}
                    subjectType={typeData}
                />)}
        </>);
};

export default SubjectTypesTeachersList;
