import {useTranslation} from "react-i18next";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Button, Chip, ChipGroup, Group, RangeSlider, SegmentedControl, Stack} from "@mantine/core";
import {IconFilter} from "@tabler/icons-react";
import {Title as MantineTitle} from "@mantine/core";
import {parseSearchParams} from "../../utils/searchUtils.ts";
import {departmentService} from "../../services";

interface SubjectFiltersProps {
  redirect?: boolean,
  degree?: number
}
export default function SubjectFilters({ redirect, degree }: SubjectFiltersProps) {
  const { t } = useTranslation(undefined, {keyPrefix: "Curriculum"});
  const navigate = useNavigate();
  
  const [params, setParams] = useSearchParams();
  
  const parsedParams = parseSearchParams(params);

  const [departmentOptions, setDepartmentOptions] = useState<string[]|undefined>(undefined);
  const [visible, setVisible] = useState(parsedParams.hasFilters());
  const [department, setDepartment] 
    = useState<string|undefined>(parsedParams.department);
  const [creditRange, setCreditRange] 
    = useState([parsedParams.minCredits ?? 0, parsedParams.maxCredits ?? 12]);
  const [difficultyRange, setDifficultyRange] 
    = useState([parsedParams.minDifficulty ?? 0, parsedParams.maxDifficulty ?? 2]);
  const [timeDemandRange, setTimeDemandRange] 
    = useState([parsedParams.minTimeDemand ?? 0, parsedParams.maxTimeDemand ?? 2]);
  const [orderBy, setOrderBy] = useState(parsedParams.orderBy ?? "name");
  const [orderByDir, setOrderByDir] = useState<string>(parsedParams.dir ?? "asc");
  const [reset, setReset] = useState(false);
  
  useEffect(() => {
    if(reset) {
      setReset(false);
      onApply();
    }
    
    if(!departmentOptions) {
      departmentService.getDepartments()
        .then(d => setDepartmentOptions(d))
        .catch(err => console.error("Failed to get departments: ", err));
    }
    
  }, [department, creditRange, difficultyRange, timeDemandRange, orderBy, orderByDir]);

  const maxCredits = 12;
  const minCredits = 0;
  const creditMarks = [];
  for(let i = minCredits; i <= maxCredits; i += 3) {
    creditMarks.push({value: i, label: i.toString()});
  }

  const difficultyMarks = [
    {value: 0, label: t("easy")},
    {value: 1, label: t("medium")},
    {value: 2, label: t("hard")}
  ];

  const timeDemandMarks = [
    {value: 0, label: t("low")},
    {value: 1, label: t("medium")},
    {value: 2, label: t("high")}
  ];

  const orderByOptions = [
    {value: "name", label: t("name")},
    {value: "credits", label: t("credits")},
    {value: "difficulty", label: t("difficulty")},
    {value: "timeDemand", label: t("timeDemand")},
    {value: "id", label: t("code")}
  ];

  const orderByDirOptions = [
    {value: "asc", label: `↿ ${t("ascending")}`},
    {value: "desc", label: `⇃  ${t("descending")}`}
  ];

  function onApply() {
    const paramsInit: Record<string, any> = {
      q: parsedParams.q,
      department,
      minCredits: creditRange[0] > 0 ? creditRange[0].toString() : undefined,
      maxCredits: creditRange[1] < 12 ? creditRange[1].toString() : undefined,
      minDifficulty: difficultyRange[0] > 0 ? difficultyRange[0].toString() : undefined,
      maxDifficulty: difficultyRange[1] < 2 ? difficultyRange[1].toString() : undefined,
      minTimeDemand: timeDemandRange[0] > 0 ? timeDemandRange[0].toString() : undefined,
      maxTimeDemand: timeDemandRange[1] < 2 ? timeDemandRange[1].toString() : undefined,
      ob: orderBy != "name" ? orderBy : undefined,
      dir: orderByDir != "asc" ? orderByDir.toString() : undefined,
      degree: parsedParams.degree ?? degree
    };
    
    //Remove undefined values
    Object.keys(paramsInit).forEach(key => {
      if(paramsInit[key] === undefined) delete paramsInit[key];
    });
    
    const params = createSearchParams(paramsInit);
    
    console.log(params.toString());
    
    if(redirect) {
      navigate(`/search?${params}`)
    } else {
      setParams(params);
    }
  }
  
  async function onReset() {
    setDepartment(undefined);
    setCreditRange([0, 12]);
    setDifficultyRange([0, 2]);
    setTimeDemandRange([0, 2]);
    setOrderBy("name");
    setOrderByDir("asc");
    setReset(true);
  }
  
  function onChangeDepartment(value: string|string[]) {
    if(typeof value === "string") setDepartment(value);
    else if(value.length === 0) setDepartment(undefined);
    else setDepartment(value[0]);
  }

  return <>
    <Button onClick={() => setVisible(!visible)} ml="1rem" mb="1rem" variant="outline" bg="white" color="grey" mt="2rem">
      <IconFilter size={20} style={{marginRight: "0.5rem"}}/>
      {t("filter")}
    </Button>
    { visible &&
        <Stack mx="3rem">
          { departmentOptions &&
              <Stack>
                <MantineTitle order={4}>{t("department")}</MantineTitle>
                <ChipGroup onChange={onChangeDepartment} value={department}>
                  <Group gap="0.5rem">
                    { departmentOptions.map(dept =>
                      <Chip key={dept} value={dept} variant="outline">{dept}</Chip>
                    )}
                  </Group>
                </ChipGroup>
              </Stack>
          }
            <Group w="100%" gap="4rem" align="start">
              <Stack>
                <MantineTitle order={4}>{t("credits")}</MantineTitle>
                <RangeSlider 
                  defaultValue={creditRange as [number, number]}
                  step={3}
                  min={minCredits}
                  max={maxCredits}
                  minRange={0}
                  label={null}
                  marks={creditMarks}
                  onChange={setCreditRange}

                  mb="2rem"
                  w="225px"
                />
              </Stack>
              <Stack>
                <MantineTitle order={4}>{t("difficulty")}</MantineTitle>
                <RangeSlider
                  defaultValue={difficultyRange as [number, number]}
                  step={1}
                  min={0}
                  max={2}
                  minRange={0}
                  label={null}
                  marks={difficultyMarks}
                  onChange={setDifficultyRange}

                  mb="2rem"
                  w="225px"
                />
              </Stack>
            <Stack>
              <MantineTitle order={4}>{t("timeDemand")}</MantineTitle>
              <RangeSlider
                defaultValue={timeDemandRange as [number, number]}
                step={1}
                min={0}
                max={2}
                minRange={0}
                label={null}
                marks={timeDemandMarks}
                onChange={setTimeDemandRange}

                mb="2rem"
                w="225px"
              />
            </Stack>
            </Group>
            <Stack w="500px">
              <MantineTitle order={4}>{t('orderBy')}</MantineTitle>
              <SegmentedControl data={orderByOptions} defaultValue={orderBy} onChange={setOrderBy} mb="-1rem" mt="-0.5rem"/>
              <SegmentedControl data={orderByDirOptions} defaultValue={orderByDir} onChange={setOrderByDir} />
            </Stack>
            <Group>
              <Button onClick={onReset} variant="light">{t("reset")}</Button>
              <Button onClick={onApply} color="blue" w="130px">{t("applyFilters")}</Button>
            </Group>
        </Stack>
    }
  </>
}
