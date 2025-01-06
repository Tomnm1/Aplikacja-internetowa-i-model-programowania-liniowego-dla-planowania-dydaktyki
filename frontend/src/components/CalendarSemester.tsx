import {FC, useMemo} from 'react';
import {Paper, Table, TableBody, TableCell, TableHead, TableRow} from '@mui/material';
import {Day} from '../utils/Interfaces';

interface ClusterData {
    key: {
        day: Day;
        timeRange: string;
        subjectName: string;
        teacher: string;
        classroom: number;
        classroomName: string;
        buildingName: string;
        isEvenWeek: boolean | null;
        subjectType: string;
    };
    groupCodes: string[];
}

interface SemesterTableViewProps {
    clusters: ClusterData[];
    allGroupCodes: string[];
    subjectColorMap: { [subject: string]: string };
    dayMapping: { [key in Day]: string };
    dayToIndex: { [key in Day]: number };
}

const CalendarSemester: FC<SemesterTableViewProps> = ({
                                                          clusters,
                                                          allGroupCodes,
                                                          subjectColorMap,
                                                          dayMapping,
                                                          dayToIndex,
                                                      }) => {
    const sortedGroupCodes = useMemo(() => {
        return [...allGroupCodes].sort((a, b) => {
            const aNum = parseInt(a.replace(/\D+/g, ''), 10);
            const bNum = parseInt(b.replace(/\D+/g, ''), 10);
            return (aNum - bNum) || a.localeCompare(b);
        });
    }, [allGroupCodes]);

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
        return Array.from(dayMap.keys()).sort((a, b) => dayToIndex[a] - dayToIndex[b]);
    }, [dayMap, dayToIndex]);

    return (<Paper className="overflow-scroll pt-2">
        <Table
            stickyHeader
            className="table-fixed w-full"
            sx={{borderCollapse: 'collapse'}}
        >
            <TableHead>
                <TableRow>
                    <TableCell className="w-16 text-center text-xs p-1">Dzień</TableCell>
                    <TableCell className="w-24 text-center text-xs p-1">Czas</TableCell>
                    {sortedGroupCodes.map((groupCode, idx) => (<TableCell
                        key={groupCode}
                        className={`text-center text-xs p-1 ${idx !== sortedGroupCodes.length - 1 ? 'border-r border-gray-300' : ''}`}
                    >
                        {groupCode}
                    </TableCell>))}
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

                    const sortedTimeRanges = Array.from(timeMap.keys()).sort((a, b) => {
                        const [aStart] = a.split('-');
                        const [bStart] = b.split('-');
                        return aStart.localeCompare(bStart);
                    });

                    const dayRowCount = sortedTimeRanges.length;

                    return sortedTimeRanges.map((timeRange, trIdx) => {
                        const trClusters = timeMap.get(timeRange)!;

                        const hasBothEvenAndOdd = (groupCode: string): boolean => {
                            const relevantClusters = trClusters.filter(cl => cl.groupCodes.includes(groupCode));
                            const hasEven = relevantClusters.some(cl => cl.key.isEvenWeek === true);
                            const hasOdd = relevantClusters.some(cl => cl.key.isEvenWeek === false);
                            return hasEven && hasOdd;
                        };

                        const getClusters = (groupCode: string, isEvenWeek: boolean | null) => {
                            return trClusters.filter(cl => cl.groupCodes.includes(groupCode) && cl.key.isEvenWeek === isEvenWeek);
                        };

                        const rowCells: JSX.Element[] = [];
                        let i = 0;

                        while (i < sortedGroupCodes.length) {
                            const currentGroup = sortedGroupCodes[i];

                            if (hasBothEvenAndOdd(currentGroup)) {
                                const evenClusters = getClusters(currentGroup, true);
                                const oddClusters = getClusters(currentGroup, false);

                                rowCells.push(<TableCell
                                    key={`both-${day}-${timeRange}-${i}`}
                                    className={`align-middle p-0 ${i !== sortedGroupCodes.length - 1 ? 'border-r border-gray-300' : ''}`}
                                    style={{
                                        backgroundColor: '#fff',
                                    }}
                                    colSpan={1}
                                >
                                    <div
                                        className="flex flex-col h-full justify-center items-center text-center text-xs">
                                        {evenClusters.length > 0 && (<div
                                            key={`even-${day}-${timeRange}-${i}`}
                                            className="flex flex-col w-full justify-center items-center text-center"
                                            style={{
                                                backgroundColor: subjectColorMap[evenClusters[0].key.subjectName] || '#fff',
                                            }}
                                        >
                                            <div className="font-bold text-xs">
                                                {evenClusters[0].key.subjectName}
                                            </div>
                                            <div className="text-xs">
                                                {evenClusters[0].key.subjectType}
                                            </div>
                                            <div className="text-xs">
                                                {evenClusters[0].key.teacher}
                                                <br/>
                                                Tydzień parzysty
                                            </div>
                                            <div className="font-bold text-xs mt-0.5">
                                                {`${evenClusters[0].key.classroomName} (${evenClusters[0].key.buildingName})`}
                                            </div>
                                        </div>)}

                                        {oddClusters.length > 0 && (<div
                                            key={`odd-${day}-${timeRange}-${i}`}
                                            className="flex flex-col w-full justify-center items-center text-center"
                                            style={{
                                                backgroundColor: subjectColorMap[oddClusters[0].key.subjectName] || '#fff',
                                            }}
                                        >
                                            <div className="font-bold text-xs">
                                                {oddClusters[0].key.subjectName}
                                            </div>
                                            <div className="text-xs">
                                                {oddClusters[0].key.subjectType}
                                            </div>
                                            <div className="text-xs">
                                                {oddClusters[0].key.teacher}
                                                <br/>
                                                Tydzień nieparzysty
                                            </div>
                                            <div className="font-bold text-xs mt-0.5">
                                                {`${oddClusters[0].key.classroomName} (${oddClusters[0].key.buildingName})`}
                                            </div>
                                        </div>)}
                                    </div>
                                </TableCell>);
                                i += 1;
                            } else {
                                const currentCluster = trClusters.find(cl => cl.groupCodes.includes(currentGroup));

                                if (!currentCluster) {
                                    rowCells.push(<TableCell
                                        key={`empty-${day}-${timeRange}-${i}`}
                                        className={`p-0 ${i !== sortedGroupCodes.length - 1 ? 'border-r border-gray-300' : ''}`}
                                    />);
                                    i += 1;
                                    continue;
                                }

                                const subjectName = currentCluster.key.subjectName;
                                const subjectType = currentCluster.key.subjectType;
                                const teacher = currentCluster.key.teacher;
                                const classroom = currentCluster.key.classroom;
                                const classroomName = currentCluster.key.classroomName;
                                const buildingName = currentCluster.key.buildingName;
                                const isEvenWeek = currentCluster.key.isEvenWeek;

                                let span = 1;
                                let j = i + 1;
                                while (j < sortedGroupCodes.length) {
                                    const nextGroup = sortedGroupCodes[j];
                                    if (hasBothEvenAndOdd(nextGroup)) break;

                                    const nextCluster = trClusters.find(cl => cl.groupCodes.includes(nextGroup));
                                    if (!nextCluster) break;

                                    if (nextCluster.key.subjectName === subjectName && nextCluster.key.subjectType === subjectType && nextCluster.key.teacher === teacher && nextCluster.key.classroom === classroom && nextCluster.key.isEvenWeek === isEvenWeek) {
                                        span += 1;
                                        j += 1;
                                    } else {
                                        break;
                                    }
                                }

                                rowCells.push(<TableCell
                                    key={`cluster-${day}-${timeRange}-${i}`}
                                    className={`align-middle p-0 ${i + span !== sortedGroupCodes.length ? 'border-r border-gray-300' : ''}`}
                                    style={{
                                        backgroundColor: subjectColorMap[subjectName] || '#fff',
                                    }}
                                    colSpan={span}
                                >
                                    <div
                                        className="flex flex-col h-full justify-center items-center text-center text-xs">
                                        <div className="font-bold text-xs">
                                            {subjectName}
                                        </div>
                                        <div className="text-xs">
                                            {subjectType}
                                        </div>
                                        <div className="text-xs">
                                            {teacher}
                                            {isEvenWeek !== null && (<>
                                                <br/>
                                                {isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                            </>)}
                                        </div>
                                        <div className="font-bold text-xs mt-0.5">
                                            {`${classroomName} (${buildingName})`}
                                        </div>
                                    </div>
                                </TableCell>);
                                i += span;
                            }
                        }

                        return (<TableRow
                            key={`${day}-${timeRange}-${trIdx}`}
                            sx={trIdx === sortedTimeRanges.length - 1 ? {borderBottom: '2px solid gray'} : {}}
                        >
                            {trIdx === 0 && (<TableCell
                                rowSpan={dayRowCount}
                                className="relative align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs"
                            >
                                <div
                                    className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 -rotate-90">
                                    {dayMapping[day]}
                                </div>
                            </TableCell>)}
                            <TableCell className="align-middle text-center p-1 text-xs">
                                {timeRange}
                            </TableCell>
                            {rowCells}
                        </TableRow>);
                    });
                })}
            </TableBody>
        </Table>
    </Paper>);
};

export default CalendarSemester;
