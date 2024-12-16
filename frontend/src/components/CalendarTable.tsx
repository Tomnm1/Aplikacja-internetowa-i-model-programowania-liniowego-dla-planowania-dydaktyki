import {FC, useMemo} from 'react';
import {Paper, Table, TableBody, TableCell, TableHead, TableRow,} from '@mui/material';
import {Day} from '../utils/Interfaces';

interface ClusterData {
    key: {
        day: Day;
        timeRange: string;
        subjectName: string;
        teacher: string;
        classroom: string;
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

const CalendarTable: FC<SemesterTableViewProps> = ({
                                                       clusters, allGroupCodes, subjectColorMap, dayMapping, dayToIndex,
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

    return (<Paper className="overflow-x-auto overflow-auto pt-2">
            <Table
                stickyHeader
                className="table-fixed w-full"
                sx={{borderCollapse: 'collapse'}}
            >
                <TableHead>
                    <TableRow>
                        <TableCell className="w-[40px] text-center text-xs p-1">
                            Dzień
                        </TableCell>
                        <TableCell className="w-[100px] text-center text-xs p-1">
                            Czas
                        </TableCell>
                        {sortedGroupCodes.map((groupCode, idx) => (<TableCell
                                key={groupCode}
                                className={`text-center text-xs p-1 ${idx === sortedGroupCodes.length - 1 ? '' : 'border-r border-gray-300'}`}
                            >
                                {groupCode}
                            </TableCell>))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {sortedDays.map((d) => {
                        const dayClusters = dayMap.get(d)!;

                        const timeMap = new Map<string, ClusterData[]>();
                        dayClusters.forEach((cl) => {
                            const tr = cl.key.timeRange;
                            if (!timeMap.has(tr)) {
                                timeMap.set(tr, []);
                            }
                            timeMap.get(tr)!.push(cl);
                        });

                        const sortedTimeRanges = Array.from(timeMap.keys()).sort((a, b) => {
                            const [aStart] = a.split('-');
                            const [bStart] = b.split('-');
                            return aStart.localeCompare(bStart);
                        });

                        const dayRowCount = sortedTimeRanges.length;
                        if (sortedTimeRanges.length === 0) {
                            return (<TableRow key={d}>
                                    <TableCell
                                        rowSpan={dayRowCount}
                                        className="align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs"
                                    >
                                        <div
                                            className="align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs [transform:rotate(-90deg)]">
                                            {dayMapping[d]}
                                        </div>
                                    </TableCell>
                                    <TableCell className="align-middle text-center p-1 text-xs">
                                        -
                                    </TableCell>
                                    {sortedGroupCodes.map((groupCode, groupIdx) => (<TableCell
                                            key={`empty-${d}-${groupCode}`}
                                            className={`p-1 ${groupIdx === sortedGroupCodes.length - 1 ? '' : 'border-r border-gray-300'}`}
                                        />))}
                                </TableRow>);
                        }

                        return sortedTimeRanges.map((tr, trIdx) => {
                            const trClusters = timeMap.get(tr)!;

                            const rowCells: JSX.Element[] = [];

                            sortedGroupCodes.forEach((groupCode, groupIdx) => {
                                const clustersForGroup = trClusters.filter((cl) => cl.groupCodes.includes(groupCode));

                                if (clustersForGroup.length === 0) {
                                    rowCells.push(<TableCell
                                        key={`empty-${d}-${tr}-${groupIdx}`}
                                        className="p-1"
                                    />);
                                } else if (clustersForGroup.length === 1) {
                                    const cl = clustersForGroup[0];
                                    const bgColor = subjectColorMap[cl.key.subjectName] || '#fff';
                                    rowCells.push(<TableCell
                                        key={`cluster-${d}-${tr}-${groupIdx}`}
                                        className="align-middle p-1"
                                        style={{
                                            backgroundColor: bgColor,
                                        }}
                                        rowSpan={trClusters.length > 1 ? 1 : undefined}
                                    >
                                        <div
                                            className="flex flex-col h-full justify-center items-center text-center text-xs">
                                            <div className="font-bold">
                                                {cl.key.subjectName}
                                            </div>
                                            <div className="text-xs">
                                                {cl.key.subjectType}
                                            </div>
                                            <div className="text-xs">
                                                {cl.key.teacher}
                                                {cl.key.isEvenWeek !== null && (<>
                                                        <br/>
                                                        {cl.key.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                                    </>)}
                                            </div>
                                            <div className="font-bold text-xs mt-0.5">
                                                {cl.key.classroom}
                                            </div>
                                        </div>
                                    </TableCell>);
                                } else {
                                    rowCells.push(<TableCell
                                        key={`multi-cluster-${d}-${tr}-${groupIdx}`}
                                        className="p-0"
                                    >
                                        <div className="flex flex-col h-full">
                                            {clustersForGroup.map((cl, idx) => {
                                                const bgColor = subjectColorMap[cl.key.subjectName]|| '#fff';
                                                return (<div
                                                        key={`sub-cluster-${idx}`}
                                                        className="flex-1 flex flex-col justify-center items-center text-center text-xs border-0"
                                                        style={{
                                                            backgroundColor: bgColor,
                                                        }}
                                                    >
                                                        <div className="font-bold text-xs">
                                                            {cl.key.subjectName}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cl.key.subjectType}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cl.key.teacher}
                                                            {cl.key.isEvenWeek !== null && (<>
                                                                    <br/>
                                                                    {cl.key.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                                                </>)}
                                                        </div>
                                                        <div className="font-bold text-xs mt-0.5">
                                                            {cl.key.classroom}
                                                        </div>
                                                    </div>);
                                            })}
                                        </div>
                                    </TableCell>);
                                }
                            });

                            const isLastTimeRange = trIdx === sortedTimeRanges.length - 1;
                            return (<TableRow
                                    key={`${d}-${tr}-${trIdx}`}
                                    sx={isLastTimeRange ? {borderBottom: '2px solid gray'} : {}}
                                >
                                    {trIdx === 0 && (<TableCell
                                            rowSpan={dayRowCount}
                                            className="relative align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs"
                                        >
                                            <div
                                                className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 -rotate-90">
                                                {dayMapping[d]}
                                            </div>
                                        </TableCell>)}
                                    <TableCell className="align-middle text-center p-1 text-xs">
                                        {tr}
                                    </TableCell>
                                    {rowCells.map((cell, idx) => (<TableCell
                                            key={`cell-${idx}`}
                                            className={cell.props.className}
                                            style={cell.props.style}
                                        >
                                            {cell.props.children}
                                        </TableCell>))}
                                </TableRow>);
                        });
                    })}
                </TableBody>
            </Table>
        </Paper>);
};

export default CalendarTable;
