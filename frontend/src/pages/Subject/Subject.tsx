import {useContext} from "react";
import AuthContext from "../../context/AuthContext.tsx";
import Landing from "../Landing/landing.tsx";
import {useTranslation} from "react-i18next";
import classes from "./Subject.module.css";
import {Card, Tabs, Text, rem} from '@mantine/core';
import {IconPhoto} from "@tabler/icons-react";
import Subject from "../../models/Subject.ts";
import {Grid} from "@mantine/core/lib";



export function Subject() {
    const { t } = useTranslation();
    const iconStyle = { width: rem(12), height: rem(12) };
    const subject: Subject = {
        id: "12.09",
        name: "Química",
        credits: 3,
        difficulty: "EASY",
        prerequisites: [],
        timeDemand: "LOW",
        reviewCount: 0,
        department: "Ciencias Exactas y Naturales",
    }
    return (
        <div className={classes.container50}>
            <div className={classes.breadcrumbArea}>

            </div>
            <div className={classes.editDeleteButtons}>
                <Text> {subject.name} - {subject.id}</Text>
                <></>
            </div>
            <Card className={classes.mainBody}>
                <Tabs defaultValue="general">
                    <Tabs.List>
                        <Tabs.Tab value="general" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.general")}  </Tabs.Tab>
                        <Tabs.Tab value="times-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.times")} </Tabs.Tab>
                        <Tabs.Tab value="professors-panel" leftSection={<IconPhoto style={iconStyle} />}> {t("Subject.classProf")} </Tabs.Tab>
                    </Tabs.List>

                    <Tabs.Panel value="general">
                        <table>
                            <tbody>
                            <tr>
                                <th>{t("Subject.department")}</th>
                                <td>{subject.department}</td>
                            </tr>
                            <tr>
                                <th>{t("Subject.credits")} </th>
                                <td>{subject.credits}</td>
                            </tr>
                            <tr>
                                <th>{t("Subject.prerequisites")}</th>
                                <td>
                                    {subject.prerequisites.length === 0? <>{t("Subject.prerequisites?")}</> : <></>}
                                    {}

                                    <c:forEach var="prereq" items="${subject.prerequisites}" varStatus="status">
                                        <a href='<c:url value="/subject/${prereq.id}"/>'><c:out value="${prereq.name}"/></a>
                                        <c:if test="${not status.last}">
                                            ,
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="subject.professors"/></th>
                                <td>
                                    <c:forEach var="professor" items="${subject.professors}" varStatus="status">
                                        <sl-badge variant="primary">
                                            <c:out value="${professor.name}"/>
                                        </sl-badge>

                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="subject.difficulty"/></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${subject.reviewStats.difficulty eq 'EASY'}">
                                            <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
                                        </c:when>
                                        <c:when test="${subject.reviewStats.difficulty eq 'MEDIUM'}">
                                            <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
                                        </c:when>
                                        <c:when test="${subject.reviewStats.difficulty eq 'HARD'}">
                                            <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
                                        </c:when>
                                        <c:otherwise>
                                            <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif"/></sl-badge>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <th><spring:message code="subject.time" /></th>
                                <td>
                                    <c:choose>
                                        <c:when test="${subject.reviewStats.timeDemanding eq 'LOW'}">
                                            <sl-badge size="medium" variant="success"><spring:message code="form.NotTimeDemanding" /></sl-badge>
                                        </c:when>
                                        <c:when test="${subject.reviewStats.timeDemanding eq 'MEDIUM'}">
                                            <sl-badge size="medium" variant="primary"><spring:message code="form.averageTimeDemand" /></sl-badge>
                                        </c:when>
                                        <c:when test="${subject.reviewStats.timeDemanding eq 'HIGH'}">
                                            <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding" /></sl-badge>
                                        </c:when>
                                        <c:otherwise>
                                            <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif" /></sl-badge>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </Tabs.Panel>
                </Tabs>
            </Card>
        </div>
    );
}

export function SubjectScreen() {
    const authContext = useContext(AuthContext);
    const isLoggedIn = authContext.isAuthenticated;
    return (
        isLoggedIn ? <Subject/> : <Landing/>
    );
}

function getSubjectPrereqs(subject: Subject){
    const prereqs: JSX.Element[] = [];
    subject.prerequisites.forEach((item) => {
            prereqs.push(
                <a href='<c:url value="/subject/${prereq.id}"/>'>{item}</a>
            );
    }
    )
    return prereqs;
}