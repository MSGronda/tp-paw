import {useEffect, useState} from "react";
import Degree from "../../models/Degree.ts";
import {Button, Center, Combobox, Group, InputBase, Loader, Transition, useCombobox} from "@mantine/core";
import {degreeService, subjectService} from "../../services";

import classes from './onboarding.module.css';
import {Default_Navbar} from "../../components/default-navbar/default_navbar.tsx";
import {
  AccordionSelector,
  AccordionSelectorItem,
  AccordionSelectorSection
} from "../../components/accordion-selector/accordionSelector.tsx";
import {Subject} from "../../models/Subject.ts";
import {userService} from "../../services";
import {Navigate, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

enum Step {
  DegreeSelection,
  SubjectSelection,
}

export default function Onboarding() {
  const navigate = useNavigate();
  const {t} = useTranslation();

  const [selectedDegree, setSelectedDegree] = useState<Degree | undefined>();
  const [selectedSubjectIds, setSelectedSubjectIds] = useState<string[]>([]);
  const [step, setStep] = useState<Step>(Step.DegreeSelection);
  const [step1Mounted, setStep1Mounted] = useState(true);
  const [step2Mounted, setStep2Mounted] = useState(false);

  const [finishedEnabled, setFinishedEnabled] = useState(true);


  if (step == 1 && step1Mounted) setStep1Mounted(false);
  if (step == 0 && step2Mounted) setStep2Mounted(false);

  async function finish() {
    try {
      setFinishedEnabled(false);
      await userService.setDegreeAndSubjects(selectedDegree!.id, selectedSubjectIds);
      navigate("/");
    } catch (e) {
      console.error(e);
      setFinishedEnabled(true);
    }
  }

  if (userService.getUserData()?.degreeId) {
    return <Navigate to={"/"}/>
  }

  return (
    <div className={classes.fullsize}>
      <Default_Navbar/>
      <div className={classes.center}>
        <Transition mounted={step1Mounted} transition="fade" onExited={() => setStep2Mounted(true)}>
          {(styles) => (
            <div style={styles}>
              <h1 className={classes.title}>{t('Onboarding.DegreeSelection.title')}</h1>
              <DegreeSelectionStep selectedDegree={selectedDegree} onSelectDegree={setSelectedDegree}/>
              <Group mt={24} justify="flex-end">
                <Button disabled={!selectedDegree} onClick={() => setStep(step + 1)}>
                  {t('Onboarding.next')}
                </Button>
              </Group>
            </div>
          )
          }
        </Transition>
        <Transition mounted={step2Mounted} transition="fade" onExited={() => setStep1Mounted(true)}>
          {(styles) => (
            <div style={styles}>
              <h1 className={classes.title}>{t('Onboarding.SubjectSelection.title')}</h1>
              <SubjectSelectionStep degree={selectedDegree!} onChangeSelected={setSelectedSubjectIds}/>
              <Group mt={24} justify="flex-end">
                <Button
                  onClick={() => setStep(step - 1)}
                >
                  {t('Onboarding.back')}
                </Button>
                <Button color="green" disabled={!finishedEnabled} onClick={finish}>
                  {t('Onboarding.finish')}
                </Button>
              </Group>
            </div>
          )}
        </Transition>
      </div>
    </div>
  );
}

interface DegreeSelectionProps {
  selectedDegree?: Degree;
  onSelectDegree: (deg: Degree) => void;
}

function DegreeSelectionStep({onSelectDegree, selectedDegree}: DegreeSelectionProps) {
  const {t} = useTranslation();
  const [degrees, setDegrees] = useState<Degree[] | null>(null);

  useEffect(() => {
    degreeService.getDegrees().then(res => setDegrees(res?.data))
      .catch(err => console.log(err));
  }, []);

  const combobox = useCombobox();

  const loading = !degrees;

  const options = degrees ? degrees.map(degree => (
    <Combobox.Option value={degree.id.toString()} key={degree.id}>
      {degree.name}
    </Combobox.Option>
  )) : [];

  return (
    <div>
      <Group grow wrap="nowrap">
        {loading ? <Center><Loader/></Center> :
          <Combobox
            // @ts-expect-error miw works
            miw={210}
            store={combobox}
            disabled={loading}
            onOptionSubmit={val => {
              const deg = degrees!.find(deg => deg.id.toString() === val)!;
              onSelectDegree?.(deg);
              combobox.closeDropdown();
            }}
          >
            <Combobox.Target>
              <InputBase
                component="button"
                type="button"
                pointer
                rightSection={<Combobox.Chevron/>}
                rightSectionPointerEvents="none"
                onClick={() => combobox.toggleDropdown()}
              >
                {selectedDegree?.name || t('Onboarding.DegreeSelection.select')}
              </InputBase>
            </Combobox.Target>
            <Combobox.Dropdown>
              <Combobox.Options>{options}</Combobox.Options>
            </Combobox.Dropdown>
          </Combobox>
        }
      </Group>
    </div>
  );
}

interface SubjectSelectionProps {
  degree: Degree;
  onChangeSelected: (selected: string[]) => void;
}

function SubjectSelectionStep({degree, onChangeSelected}: SubjectSelectionProps) {
  const {t} = useTranslation();

  const [subjectsBySemester, setSubjectsBySemester] = useState<Record<number, Subject[]>>({});

  useEffect(() => {
    subjectService.getSubjectsBySemester(degree.id).then(res => setSubjectsBySemester(res))
      .catch(err => console.log(err));
  }, [degree.id]);

  const loading = Object.keys(subjectsBySemester).length === 0;

  const sections = Object.entries(subjectsBySemester).map(entry => (
    <AccordionSelectorSection
      key={entry[0]}
      title={entry[0] == "-1" ? t('Onboarding.SubjectSelection.electives') : t('Onboarding.SubjectSelection.semester', {n: entry[0]})}
    >
      {entry[1].map(subject => (
        <AccordionSelectorItem key={subject.id} value={subject.id} title={`${subject.id} - ${subject.name}`}/>
      ))}
    </AccordionSelectorSection>
  ));

  return (
    <div>
      {loading ? <Center><Loader/></Center> :
        <AccordionSelector onChangeSelected={onChangeSelected} miw="500">
          {sections}
        </AccordionSelector>
      }
    </div>
  );
}
