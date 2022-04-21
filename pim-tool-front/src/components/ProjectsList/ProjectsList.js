import { React, useEffect, useState, } from 'react';
import { Form, Col, Row, Button, Table, Alert, Pagination } from 'react-bootstrap';
import { Link, useHistory, } from 'react-router-dom';
import axios from 'axios';
import Translate from 'react-translate-component';
import counterpart from 'counterpart';
import array from 'lodash/array';

//CSS
import './ProjectsList.css';

//local component
import en from '../../lang/en';
import fr from '../../lang/fr';

counterpart.registerTranslations('en', en);
counterpart.registerTranslations('fr', fr);

export default function ProjectsList() {
  const [projectsList, setProjectsList] = useState([]);
  const [projectIds, setProjectIds] = useState([]);
  const [keyword, setKeyword] = useState('');
  const [projectStatus, setProjectStatus] = useState('');
  const [errorMessage, setErrorMessage] = useState({ title: null, errors: null });
  const [showAlert, setShowAlert] = useState(false);
  const [activePage, setActivePage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const statusDictionary = {
    'NEW': 'New',
    'FIN': 'Finished',
    'INP': 'In progress',
    'PLA': 'Planned'
  };

  let paginationItems = [];
  for (let number = 1; number <= totalPages; number++) {
    paginationItems.push(
      <Pagination.Item key={number} active={number === activePage ? true : false} onClick={() => handleChangePage(number)}>
        {number}
      </Pagination.Item>,
    );
  }

  let history = useHistory();

  axios.interceptors.response.use((response) => response, (error) => {
    if (error.response.status === 500) {
      history.push('/error');
    }
    return Promise.reject(error);
  })

  useEffect(() => {
    let definedKeyword = localStorage.getItem('keyword');
    if (definedKeyword) {
      setKeyword(definedKeyword);
    }

    let definedProjectStatus = localStorage.getItem('projectStatus');
    if (definedProjectStatus) {
      setProjectStatus(definedProjectStatus);
    }

    getProjectsList(definedKeyword, definedProjectStatus);
  }, [])

  function handleChangePage(number) {
    setActivePage(number);
    getProjectsList(keyword, projectStatus, number);
  }

  function getProjectsList(searchKeyword, status, page=1) {
    localStorage.setItem('keyword', searchKeyword);
    localStorage.setItem('projectStatus', status);

    axios.get('/projects/find', {
      params: {
        searchKeyword: searchKeyword,
        status: status,
        page: page-1
      }
    })
      .then((res) => {
        console.log(res.data);
        setProjectsList(res.data.projects);
        setTotalPages(res.data.totalPages);
      })
      .catch((err) => {
        console.error(err.response.data);
      })
  }

  function handleResetSearch(e) {
    e.preventDefault();
    localStorage.setItem('keyword', '');
    localStorage.setItem('projectStatus', '');
    setKeyword('');
    setProjectStatus('');

    getProjectsList('', '');
  }

  function handleCheckedProject(e, projectId) {
    if (e.target.checked) {
      setProjectIds(projectIds.concat([projectId]));
    }
    else {
      setProjectIds(projectIds.filter((id) => id !== projectId));
    }
  }

  function handleRemoveProjects(e, ids) {
    e.preventDefault();
    removeProjects(ids);
  }

  function removeProjects(ids) {
    let formattedIds = '';
    ids.forEach((id, index) => {
      formattedIds += id;
      if (index !== ids.length - 1) {
        formattedIds += ',';
      }
    })
    axios.delete('/projects/delete', {
      params: {
        ids: formattedIds
      }
    })
      .then((res) => {
        if (res) {
          console.log(res);
          console.log(res.data);
          console.log(projectsList);
          console.log(array.differenceBy(projectsList, res.data, 'id'));

          setProjectsList(array.differenceBy(projectsList, res.data, 'id'));
          //reset projectIds if i remove by checking multiple project
          setProjectIds([]);
        }
      })
      .catch((err) => {
        if (err) {
          console.log(err.response.data);
          if (err.response.data.title === 'DELETED PROJECTS STATUS NOT NEW') {
            setErrorMessage(
              {
                title: err.response.data.title,
                errors: err.response.data.errors
              }
            );
            setShowAlert(true);
          }
          if (err.response.data.title === 'PROJECT NOT FOUND') {
            let invalidIds = err.response.data.errors.map(id => parseInt(id));
            let invalidProjects = projectsList.filter((project) => invalidIds.includes(project.id));
            setErrorMessage(
              {
                title: err.response.data.title,
                errors: invalidProjects.map(project => project.name)
              }
            );
            setShowAlert(true);
          }
        }
      })
  }

  return (
    <div>
      <p className='projects-list-legend'>
        <Translate content='projectsList.title' />
      </p>

      <Form.Group as={Row} className='mb-3'>
        <Col sm='4' className='input-padding'>
          <Translate
            component='input'
            type='text'
            value={keyword}
            className='form-control'
            attributes={{
              placeholder: 'projectsList.searchProjectPlaceHolder'
            }}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </Col>

        <Col sm='2' className='input-padding'>
          <Form.Select
            className="placeholder-select-font"
            value={projectStatus}
            defaultValue=""
            onChange={(e) => setProjectStatus(e.target.value)}
          >
            <Translate
              component='option'
              value=""
              disabled
              hidden
              content='projectsList.filterProjectStatus'
            />
            {/* <option value=''>all</option> */}
            <option value='NEW'>NEW</option>
            <option value='PLA'>PLA</option>
            <option value='INP'>INP</option>
            <option value='FIN'>FIN</option>
          </Form.Select>
        </Col>

        <Col sm='2' className='input-padding'>
          <Button id='submit-btn' onClick={() => getProjectsList(keyword, projectStatus)}>
            <Translate content='projectsList.searchProjectButton' />
          </Button>
        </Col>

        <Col sm='2' className='input-padding'>
          <a href='/' className='text-decoration-none' onClick={(e) => handleResetSearch(e)}>
            <Translate content='projectsList.resetSearchLink' />
          </a>
        </Col>
      </Form.Group>

      <Alert variant='danger' dismissible show={showAlert} onClose={() => setShowAlert(false)}>
        {
          errorMessage.title === 'DELETED PROJECTS STATUS NOT NEW' ?
            <p className='text-danger'>
              <Translate content='projectsList.deletedProjectsStatusNotNew' />
              <span>{errorMessage.errors.toString()}</span>
            </p>
            : null
        }
        {
          errorMessage.title === 'PROJECT NOT FOUND' ?
            <p className='text-danger'>
              <Translate content='projectsList.projectNotFound' />
              <span>{errorMessage.errors.toString()}</span>
            </p>
            : null
        }
      </Alert>

      <Table hover responsive bordered>
        <thead>
          <tr>
            <th style={{ width: '3%' }}></th>
            <th>
              <Translate content='projectsList.projectNumber' />
            </th>
            <th style={{ width: '41%' }}>
              <Translate content='projectsList.projectName' />
            </th>
            <th style={{ width: '14%' }}>
              <Translate content='projectsList.status' />
            </th>
            <th style={{ width: '25%' }}>
              <Translate content='projectsList.customer' />
            </th>
            <th style={{ width: '12%' }} className='text-center'>
              <Translate content='projectsList.startDate' />
            </th>
            <th style={{ width: '5%' }} className='text-center'>
              <Translate content='projectsList.deleteProject' />
            </th>
          </tr>
        </thead>
        <tbody>
          {
            projectsList.map(project =>
              <tr key={project.id}>
                <td>
                  <input className="form-check-input custom-checkbox" type="checkbox" onChange={(e) => handleCheckedProject(e, project.id)} />
                </td>
                <td className='text-end'>
                  <Link to={`projects/${project.id}/edit`}>
                    {project.projectNumber}
                  </Link>
                </td>
                <td>{project.name}</td>
                <td>{statusDictionary[project.status]}</td>
                <td>{project.customer}</td>
                <td className='text-center'>{project.startDate}</td>
                <td className='text-center'>
                  {
                    project.status === 'NEW' ?
                      <a href='/' onClick={(e) => handleRemoveProjects(e, [project.id])}>
                        <i className='fa fa-trash text-danger' aria-hidden='true'></i>
                      </a>
                      : null
                  }
                </td>
              </tr>
            )
          }
        </tbody>
      </Table>

      {
        projectIds.length !== 0 ?
          <Alert variant='primary'>
            <span className='text-primary selected-text'>{projectIds.length} items selected</span>
            <span className='text-danger float-end selected-text'>
              <span>deleted selected item </span>
              <a href='/' onClick={(e) => handleRemoveProjects(e, projectIds)}>
                <i className='fa fa-trash text-danger' aria-hidden='true'></i>
              </a>
            </span>
          </Alert>
          : null
      }

      <Pagination className='justify-content-end'>
        {activePage !== 1 ? 
          <Pagination.First onClick={() => handleChangePage(activePage-1)} />
          : <Pagination.First disabled />
        }
        
        {paginationItems}
        
        {activePage !== totalPages ?
          <Pagination.Last onClick={() => handleChangePage(activePage+1)} />
          : <Pagination.Last disabled />
        }
      </Pagination>
    </div>
  )
}