import { Pagination } from '@mantine/core';

const PaginationComponent = (props: any) => {

  const handleChange = (value: number) => {
    // Update the URL with the new page number
    const queryParams = new URLSearchParams(window.location.search);
    queryParams.set('page', value.toString());
    window.history.replaceState(null, '', `${window.location.pathname}?${queryParams}`);
    props.setPage(value);
  };

  return (
    <div>
      <Pagination total={parseInt(props.lastPage)} 
                      variant="outlined" 
                      value={parseInt(props.page)}
                      onChange={handleChange}/>
    </div>
  );
}

export default PaginationComponent;
