import { FC, useMemo } from 'react';
import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
} from '@mui/material';
import { BackendClassroom, Day } from '../utils/Interfaces';

interface ClusterData {
    key: {
        day: Day;
        timeRange: string;
        subjectName: string;
        teacher: string;
        classroom: number;
        isEvenWeek: boolean | null;
        subjectType: string;
    };
    groupCodes: string[];
}

interface ClassroomTableProps {
    clusters: ClusterData[];
    allClassrooms: BackendClassroom[];
    subjectColorMap: { [subject: string]: string };
    dayMapping: { [key in Day]: string };
    dayToIndex: { [key in Day]: number };
}

const ClassroomTable: FC<ClassroomTableProps> = ({
                                                     clusters,
                                                     allClassrooms,
                                                     subjectColorMap,
                                                     dayMapping,
                                                     dayToIndex,
                                                 }) => {
    const sortedClassrooms = useMemo(() => {
        return [...allClassrooms].sort((a, b) =>
            a.classroomID!.toString().localeCompare(b.classroomID!.toString())
        );
    }, [allClassrooms]);
    const dayMap = useMemo(() => {
        const map = new Map<Day, ClusterData[]>();
        clusters.forEach((c) => {
            if (!map.has(c.key.day)) {
                map.set(c.key.day, []);
            }
            map.get(c.key.day)!.push(c);
        });
        return map;
    }, [clusters]);
    const sortedDays = useMemo(() => {
        return Array.from(dayMap.keys()).sort(
            (a, b) => dayToIndex[a] - dayToIndex[b]
        );
    }, [dayMap, dayToIndex]);

    return (
        <Paper className="pt-2">
            <TableContainer
                className="overflow-y-auto overflow-x-scroll"
                style={{ maxHeight: '85vh' }}
            >
                <Table stickyHeader className="table-fixed">
                    <TableHead>
                        <TableRow>
                            <TableCell
                                className="w-16 text-center text-xs p-1"
                                style={{
                                    position: 'sticky',
                                    left: 0,
                                    backgroundColor: '#fff',
                                    zIndex: 3,
                                }}
                            >
                                Dzień
                            </TableCell>
                            <TableCell
                                className="w-24 text-center text-xs p-1 border-r border-gray-300"
                                style={{
                                    position: 'sticky',
                                    left: 64,
                                    backgroundColor: '#fff',
                                    zIndex: 3,
                                }}
                            >
                                Czas
                            </TableCell>
                            {sortedClassrooms.map((classroom) => {
                                const s = `${classroom.code} (${classroom.building.code})`;
                                return (
                                    <TableCell
                                        key={classroom.classroomID}
                                        className="w-32 text-center text-xs p-1 border-r border-gray-300 border-b-gray-600"
                                    >
                                        {s}
                                    </TableCell>
                                );
                            })}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sortedDays.map((day) => {
                            const dayClusters = dayMap.get(day)!;
                            const timeMap = new Map<string, ClusterData[]>();

                            dayClusters.forEach((cluster) => {
                                const tr = cluster.key.timeRange;
                                if (!timeMap.has(tr)) {
                                    timeMap.set(tr, []);
                                }
                                timeMap.get(tr)!.push(cluster);
                            });

                            const sortedTimeRanges = Array.from(timeMap.keys()).sort(
                                (a, b) => {
                                    const [aStart] = a.split('-');
                                    const [bStart] = b.split('-');
                                    return aStart.localeCompare(bStart);
                                }
                            );

                            return sortedTimeRanges.map((timeRange, trIdx) => (
                                <TableRow key={`${day}-${timeRange}-${trIdx}`}>
                                    {trIdx === 0 && (
                                        <TableCell
                                            rowSpan={sortedTimeRanges.length}
                                            className="relative align-middle text-center font-bold border-0 p-0 whitespace-nowrap text-xs"
                                            style={{
                                                position: 'sticky',
                                                left: 0,
                                                backgroundColor: '#fff',
                                                zIndex: 1,
                                            }}
                                        >
                                            <div
                                                className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 -rotate-90"
                                                style={{ whiteSpace: 'nowrap' }}
                                            >
                                                {dayMapping[day]}
                                            </div>
                                        </TableCell>
                                    )}
                                    <TableCell
                                        className="align-middle text-center p-0 text-xs border-r border-gray-600 border-b-gray-300"
                                        style={{
                                            position: 'sticky',
                                            left: 64,
                                            backgroundColor: '#fff',
                                            zIndex: 1,
                                        }}
                                    >
                                        {timeRange}
                                    </TableCell>

                                    {sortedClassrooms.map((classroom) => {
                                        const clustersForClassroom = timeMap
                                            .get(timeRange)
                                            ?.filter(
                                                (cl) =>
                                                    cl.key.classroom ===
                                                    classroom.classroomID!
                                            );
                                        if (!clustersForClassroom || clustersForClassroom.length === 0) {
                                            return (
                                                <TableCell
                                                    key={`empty-${day}-${timeRange}-${classroom.classroomID}`}
                                                    className="p-0 border-r border-gray-300"
                                                />
                                            );
                                        }

                                        return (
                                            <TableCell
                                                key={`cluster-${day}-${timeRange}-${classroom.classroomID}`}
                                                className="align-middle p-0 border-r border-gray-300"
                                                style={{
                                                    backgroundColor:
                                                        subjectColorMap[clustersForClassroom[0].key.subjectName] ||
                                                        '#fff',
                                                }}
                                            >
                                                {clustersForClassroom.map((cluster, index) => (
                                                    <div
                                                        key={`cluster-${day}-${timeRange}-${classroom.classroomID}-week-${cluster.key.isEvenWeek}-${index}`}
                                                        className="flex flex-col h-full justify-center items-center text-center text-xs p-1"

                                                    >
                                                        <div className="font-bold text-xs">
                                                            {cluster.key.subjectName}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cluster.key.subjectType}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cluster.key.teacher}
                                                            {cluster.key.isEvenWeek !== null && (
                                                                <>
                                                                    <br />
                                                                    {cluster.key.isEvenWeek
                                                                        ? 'Tydzień parzysty'
                                                                        : 'Tydzień nieparzysty'}
                                                                </>
                                                            )}
                                                        </div>
                                                    </div>
                                                ))}
                                            </TableCell>
                                        );
                                    })}
                                </TableRow>
                            ));
                        })}
                    </TableBody>
                </Table>
            </TableContainer>
        </Paper>
    );
};

export default ClassroomTable;
