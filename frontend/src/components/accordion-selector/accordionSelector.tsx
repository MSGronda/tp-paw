import {Accordion, AccordionItemProps, AccordionProps, Checkbox, Container, Divider, Group, Text} from "@mantine/core";
import {useState} from "react";

interface AccordionSelectorProps extends AccordionProps<true> {
  children: React.ReactElement<AccordionSelectorSectionProps>[] | React.ReactElement<AccordionSelectorSectionProps>;
  onChangeSelected?: (selected: string[]) => void;
}

export function AccordionSelector(props: AccordionSelectorProps) {
  const [selected, setSelected] = useState<Record<number, string[]>>([]);

  let children = Array.isArray(props.children) ? props.children : [props.children];
  children = children.map((child, i) => (
    <AccordionSelectorSection {...child.props} key={i}
                              onChangeSelected={(selected) => onChildrenChangeSelected(i, selected)}>
      {child.props.children}
    </AccordionSelectorSection>
  ));

  function onChildrenChangeSelected(index: number, childrenSelected: string[]) {
    const newSelected = {...selected};
    newSelected[index] = childrenSelected;
    setSelected(newSelected);
    props.onChangeSelected?.(Object.values(newSelected).flat());
  }

  return (
    <Accordion {...props} variant="contained" multiple>
      {children}
    </Accordion>
  );
}

// @ts-expect-error value is set by the component
interface AccordionSelectorSectionProps extends AccordionItemProps {
  children: React.ReactElement<AccordionSelectorItemProps>[] | React.ReactElement<AccordionSelectorItemProps>;
  title: string;
  value?: string;
  onChangeSelected?: (selected: string[]) => void
}

export function AccordionSelectorSection(props: AccordionSelectorSectionProps) {
  const [selected, setSelected] = useState<string[]>([]);

  function setItem(checked: boolean, value: string) {
    const filtered = selected.filter((v) => v !== value);
    let newSelected: string[];

    if (checked) {
      newSelected = [...filtered, value];
    } else {
      newSelected = filtered;
    }

    setSelected(newSelected);
    props.onChangeSelected?.(newSelected);
  }

  let childrenArr = Array.isArray(props.children) ? props.children : [props.children];
  childrenArr = childrenArr.map((child, i) => (
    <AccordionSelectorItem {...child.props} key={i} index={i} checked={selected.includes(child.props.value)}
                           onChanged={setItem}/>
  ));

  function setAll(checked: boolean) {
    let newSelected: string[];
    
    if (!checked) {
      newSelected = [];
    } else {
      newSelected = childrenArr.map((e) => e.props.value);
    }

    setSelected(newSelected);
    props.onChangeSelected?.(newSelected);
  }

  return (
    <Accordion.Item {...props} key={props.title} value={props.title}>
      <Group pl={16} wrap="nowrap" preventGrowOverflow>
        <Checkbox
          checked={selected.length === childrenArr.length}
          indeterminate={selected.length > 0 && selected.length < childrenArr.length}
          onChange={(ev) => setAll(ev.currentTarget.checked)}
        />
        <Accordion.Control>
          <Text pr={8}>{props.title}</Text>
        </Accordion.Control>
      </Group>
      <Accordion.Panel>
        <Divider ml={16} mt={-5} mb={0} pt={0}/>
        {childrenArr}
      </Accordion.Panel>
    </Accordion.Item>
  );
}

interface AccordionSelectorItemProps {
  title: string;
  checked?: boolean;
  value: string;
  index?: number;
  onChanged?: (checked: boolean, value: string) => void;
}

export function AccordionSelectorItem({title, checked, onChanged, value, index}: AccordionSelectorItemProps) {
  return (
    <>
      {index && index > 0 ?
        <Divider mb={16} ml={16} mt={16} pt={0}/>
        : <Container mb={16} ml={16} mt={0} pt={0}/>
      }

      <Checkbox
        pl={24}
        label={title}
        checked={checked} onChange={(ev) => onChanged?.(ev.currentTarget.checked, value)}
      />
    </>
  );
}
