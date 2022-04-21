import { React, useEffect, useState } from 'react';
import { Form, Row, Col, Button, Alert, } from 'react-bootstrap';
import Translate from 'react-translate-component';
import counterpart from 'counterpart';
import axios from 'axios';
import { Link, useParams, useHistory } from 'react-router-dom';

//MUI date picker
import { isAfter } from 'date-fns';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker,
} from '@material-ui/pickers';

//CSS
import './ProjectForm.css';

//local components
import en from '../../lang/en';
import fr from '../../lang/fr';

counterpart.registerTranslations('en', en);
counterpart.registerTranslations('fr', fr);

export default function ProjectForm(props) {
  const [projectNumber, setProjectNumber] = useState({ value: '', error: null });
  const [projectName, setProjectName] = useState('');
  const [customer, setCustomer] = useState('');
  const [groupLeaderVisas, setGroupLeaderVisas] = useState([]);
  const [groupLeaderVisa, setGroupLeaderVisa] = useState('');
  const [members, setMembers] = useState({ value: '', error: null });
  const [status, setStatus] = useState('NEW');
  const [startDate, setStartDate] = useState({ value: null, error: null });
  const [endDate, setEndDate] = useState({ value: null, error: null });
  const [version, setVersion] = useState(null);
  const [showAlert, setShowAlert] = useState(false);

  let editProject = useParams();
  const [submitMethod, setSubmitMethod] = useState(null);
  const [submitUrl, setSubmitUrl] = useState(null);
  const [updateProjectError, setUpdateProjectError] = useState(null);

  let history = useHistory();

  axios.interceptors.response.use((response) => response, (error) => {
    if (error.response.status === 500) {
      history.push('/error');
    }
    return Promise.reject(error);
  })

  function getGroup() {
    let headers = {
      'Content-Type': 'application/json'
    }
    axios.get('/group/list', headers)
      .then((res) => {
        if (res.data) {
          setGroupLeaderVisas(res.data);

          if (props.title === 'New Project') {
            setGroupLeaderVisa(res.data[0]);
          }
        }
      })
      .catch((err) => {
        console.log(err);
      })
  }

  function getEditProject(id) {
    axios.get('/projects/' + id)
      .then((res) => {
        let employeeVisas = '';

        // employeeVisas received from server is string's array so I need to convert into one string each visa splitted with comma
        res.data.employeeVisas.forEach((visa, index) => {
          employeeVisas = employeeVisas + visa;
          if (index !== res.data.employeeVisas.length - 1) {
            employeeVisas += ',';
          }
        });

        setProjectNumber({ value: res.data.projectNumber, error: null });
        setProjectName(res.data.name);
        setCustomer(res.data.customer);
        setGroupLeaderVisa(res.data.groupLeaderVisa);
        setMembers({ value: employeeVisas, error: null });
        setStatus(res.data.status);
        setStartDate({ value: new Date(res.data.startDate), error: null });
        
        if (res.data.endDate !== null) {
          setEndDate({ value: new Date(res.data.endDate), error: null });
        }
        else {
          setEndDate({ value: null, error: null});
        }
        setVersion(res.data.version);
      })
      .catch((err) => {
        console.error(err.response.data);
      })
  }

  useEffect(() => {
    if (props.title === 'New Project') {
      getGroup();
      setVersion(0);
      setSubmitMethod('post');
      setSubmitUrl('/projects/create');
    }
    else {
      getGroup();
      getEditProject(editProject.id);
      setSubmitMethod('put');
      setSubmitUrl('/projects/' + editProject.id + '/update');
    }
  }, [])

  function validate() {
    // validate if required fields filled
    if (projectNumber.value
      && projectName
      && customer
      && groupLeaderVisa
      && status
      && startDate.value
    ) {
      // validate if start date is after end date
      if (startDate.value && endDate.value) {
        if (isAfter(startDate.value, endDate.value)) {
          setStartDate({ value: startDate.value, error: 'Start date cannot be after End date' });
          return false;
        }
      }
      return true;
    }
    else {
      setShowAlert(true);
      return false;
    }
  }

  function formatDate(dateTime) {
    let year = dateTime.getFullYear();
    let month = parseInt(dateTime.getMonth()) + 1;
    let date = dateTime.getDate();

    if (month < 10) {
      month = '0' + month;
    }
    if (date < 10) {
      date = '0' + date;
    }
    return year + '-' + month + '-' + date;
  }

  function formatMembers(membersVal) {
    /*
      * This function handle members which is a string contains members of project split with comma
      * Trim string (remove space at first and last of string)
      * handle last comma since when I call split to split string to array it will return an extra empty element inside   
     */
    membersVal = membersVal.trim();

    // remove whitespace
    membersVal = membersVal.replace(/\s/g, '');

    /*
      * To remove last comma
      * The / mark the beginning and end of the regular expression
      * The , matches the comma
      * The \s means whitespace characters (space, tab, etc) and the * means 0 or more
      * The $ at the end signifies the end of the string 
     */
    membersVal = membersVal.replace(/,\s*$/, '');
    return membersVal.split(',');
  }

  function submit() {
    let formattedStartDate = formatDate(startDate.value);
    let formattedEndDate = null;
    if (endDate.value) {
      formattedEndDate = formatDate(endDate.value);
    }
    let formattedMembers = null;
    if (members.value.length !== 0) {
      formattedMembers = formatMembers(members.value);
    }

    let headers = {
      'Content-Type': 'application/json'
    };
    let data = {
      'projectNumber': projectNumber.value,
      'name': projectName,
      'customer': customer,
      'groupLeaderVisa': groupLeaderVisa,
      'employeeVisas': formattedMembers,
      'status': status,
      'startDate': formattedStartDate,
      'endDate': formattedEndDate,
      'version': version
    };
    axios({
      method: submitMethod,
      url: submitUrl,
      data: data,
      headers: headers
    })
      .then((res) => {
        if (res) {
          console.log(res.data);
          history.push('/');
        }
      })
      .catch((err) => {
        // handle errors
        if (err.response) {
          console.log(err.response.data);
          let errorMessage = err.response.data.title;
          if (errorMessage === 'INVALID VISAS') {
            let invalidVisas = ' { ' + err.response.data.errors[0] + ' } ';
            setMembers({ value: members.value, error: invalidVisas });
          }
          if (errorMessage === 'PROJECT NUMBER EXISTED') {
            setProjectNumber({ value: projectNumber.value, error: true });
          }
          if (errorMessage === 'OPTIMISTIC LOCKING') {
            setUpdateProjectError(true);
          }
        }
      })
  }

  function handleSubmit(e) {
    e.preventDefault();

    // remove error message on submit
    setProjectNumber({ value: projectNumber.value, error: null });
    setMembers({ value: members.value, error: null });
    setStartDate({ value: startDate.value, error: null });

    if (validate()) {
      submit();
    }
  }

  return (
    <div>
      <p className='new-project-legend'>
        <Translate content={props.title === 'New Project' ? 'projectForm.newProjectTitle' : 'projectForm.editProjectTitle'} />
      </p>

      <Alert variant="danger" dismissible show={showAlert} onClose={() => setShowAlert(false)}>
        <p className='text-danger'>
          <Translate content='projectForm.missMandatoryFields' />
        </p>
      </Alert>

      <Form>
        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.projectNumber' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='2' className='input-padding'>
            <Form.Control
              className='form-input'
              type='text'
              value={projectNumber.value}
              onChange={(e) => setProjectNumber({ value: e.target.value, error: projectNumber.error })}
              disabled={props.title !== 'New Project' ? true : false}
            />
          </Col>
          {
            projectNumber.error !== null ?
              <Col sm='6' className='mt-2'>
                <Translate className='text-danger' content='projectForm.projectNumberExisted' />
              </Col>
              : null
          }
        </Form.Group>

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.projectName' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='8' className='input-padding'>
            <Form.Control
              className='form-input'
              type='text'
              value={projectName}
              onChange={(e) => setProjectName(e.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.customer' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='8' className='input-padding'>
            <Form.Control
              className='form-input'
              type='text'
              value={customer}
              onChange={(e) => setCustomer(e.target.value)}
            />
          </Col>
        </Form.Group>

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.group' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='2' className='input-padding'>
            <Form.Select
              defaultValue={groupLeaderVisa}
              value={groupLeaderVisa}
              onChange={(e) => setGroupLeaderVisa(e.target.value)}>
              {groupLeaderVisas.map(visa => <option key={visa} value={visa}>{visa}</option>)}
            </Form.Select>
          </Col>
        </Form.Group>

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.members' />
          </Form.Label>
          <Col sm='8' className='input-padding'>
            <Form.Control
              className='form-input'
              type='text'
              value={members.value}
              onChange={(e) => setMembers({ value: e.target.value, error: members.error })}
            />
          </Col>
        </Form.Group>

        {
          members.error !== null ?
            <Row style={{ marginTop: '-38px', marginBottom: '38px' }}>
              <Col sm='1'></Col>
              <Col sm='6' className='mt-2'>
                <Translate className='text-danger' content='projectForm.membersNotExisted' />
                <span className='text-danger'> {members.error}</span>
              </Col>
            </Row>
            : null
        }

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.status' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='2' className='input-padding'>
            <Form.Select
              defaultValue={status}
              value={status}
              onChange={(e) => setStatus(e.target.value)}>
              <option value='NEW'>NEW</option>
              <option value='PLA'>PLA</option>
              <option value='INP'>INP</option>
              <option value='FIN'>FIN</option>
            </Form.Select>
          </Col>
        </Form.Group>

        <Form.Group as={Row} className='form-space'>
          <Col sm='1'></Col>
          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.startDate' />
            <span className='form-required'>*</span>
          </Form.Label>
          <Col sm='2' className='input-padding'>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
              <Grid container>
                <KeyboardDatePicker
                  style={{ border: '1px solid #ced4da', borderRadius: '5px', padding: '0px 3px', width: '100%' }}
                  disableToolbar
                  variant='inline'
                  format='MM/dd/yyyy'
                  // margin='dense'
                  id='date-picker-inline'
                  // label='Date picker inline'
                  value={startDate.value}
                  onChange={(value) => setStartDate({ value: value, error: startDate.error })}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                  InputProps={{ disableUnderline: true }}
                />
              </Grid>
            </MuiPickersUtilsProvider>
          </Col>

          <Col sm='1'></Col>

          <Form.Label column sm='2' className='form-label'>
            <Translate content='projectForm.endDate' />
          </Form.Label>
          <Col sm='2' className='input-padding'>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
              <Grid container>
                <KeyboardDatePicker
                  style={{ border: '1px solid #ced4da', borderRadius: '5px', padding: '0px 3px', width: '100%' }}
                  disableToolbar
                  variant='inline'
                  format='MM/dd/yyyy'
                  // margin='dense'
                  id='date-picker-inline'
                  // label='Date picker inline'
                  value={endDate.value}
                  onChange={(value) => setEndDate({ value: value, error: endDate.error })}
                  KeyboardButtonProps={{
                    'aria-label': 'change date',
                  }}
                  InputProps={{ disableUnderline: true }}
                />
              </Grid>
            </MuiPickersUtilsProvider>
          </Col>

          <Row>
            <Col sm='1'></Col>
            <Col sm='3'>
              {
                startDate.error === 'Start date cannot be after End date' ?
                  <Translate className='text-danger' content='projectForm.startDateAfterEndDate' />
                  : null
              }
            </Col>
          </Row>
        </Form.Group>

        <Row>
          <Col sm='6'>
            {
              updateProjectError !== null ? <Translate className='text-danger' content='projectForm.updateProjectError' /> : null
            }
          </Col>
          <Col sm='2'>
            <Link to='/'>
              <Button id='cancel-btn' type='button'>
                <Translate content='projectForm.cancel' />
              </Button>
            </Link>
          </Col>
          <Col sm='2'>
            <Button id='submit-btn' type='submit' onClick={(e) => handleSubmit(e)}>
              <Translate content={props.title === 'New Project' ? 'projectForm.createProject' : 'projectForm.updateProject'} />
            </Button>
          </Col>
        </Row>
      </Form>
    </div>
  )
}