import { Pagination } from '@mantine/core';

interface PaginationProps {
  lastPage: string|number;
  page?: string|number;
  setPage: (value: number) => void;
}

const PaginationComponent = (props: PaginationProps) => {

  const handleChange = (value: number) => {
    // Update the URL with the new page number
    const queryParams = new URLSearchParams(window.location.search);
    queryParams.set('page', value.toString());
    window.history.replaceState(null, '', `${window.location.pathname}?${queryParams}`);
    props.setPage(value);

    // Scroll to the top of the page
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };
  
  const pageInt = props.page ? parseInt(props.page.toString()) : 1;
  const lastPageInt = parseInt(props.lastPage.toString());

  return (
    <div>
      <Pagination total={lastPageInt} 
                      variant="outlined" 
                      value={pageInt}
                      onChange={handleChange}/>
    </div>
  );
}

export default PaginationComponent;
