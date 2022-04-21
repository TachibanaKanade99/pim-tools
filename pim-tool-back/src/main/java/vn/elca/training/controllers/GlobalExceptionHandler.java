package vn.elca.training.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.elca.training.model.dto.ExceptionDto;
import vn.elca.training.model.exception.ApplicationUnexpectedException;
import vn.elca.training.model.exception.DeletedProjectStatusNotNewException;
import vn.elca.training.model.exception.EmployeeVisaDoesNotExistException;
import vn.elca.training.model.exception.GroupLeaderDoesNotExistException;
import vn.elca.training.model.exception.ProjectNotFoundException;
import vn.elca.training.model.exception.ProjectNumberAlreadyExistsException;
import vn.elca.training.model.exception.ProjectStatusDoesNotExistException;
import vn.elca.training.model.exception.StartDateIsAfterEndDateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ProjectNumberAlreadyExistsException.class,
            EmployeeVisaDoesNotExistException.class,
            ApplicationUnexpectedException.class,
            ObjectOptimisticLockingFailureException.class,
            GroupLeaderDoesNotExistException.class,
            ProjectStatusDoesNotExistException.class,
            StartDateIsAfterEndDateException.class,
            DeletedProjectStatusNotNewException.class,
            ProjectNotFoundException.class,
            Exception.class
    })
    public ResponseEntity<ExceptionDto> handleException(Exception ex) {
        if (ex instanceof ProjectNumberAlreadyExistsException) {
            ProjectNumberAlreadyExistsException pnaee = (ProjectNumberAlreadyExistsException) ex;
            return handleProjectNumberAlreadyExistsException(pnaee);
        }
        else if (ex instanceof EmployeeVisaDoesNotExistException) {
            EmployeeVisaDoesNotExistException evdnee = (EmployeeVisaDoesNotExistException) ex;
            return handleEmployeeVisaDoesNotExistException(evdnee);
        }
        else if (ex instanceof ObjectOptimisticLockingFailureException) {
            ObjectOptimisticLockingFailureException oolfe = (ObjectOptimisticLockingFailureException) ex;
            return handleObjectOptimisticLockingFailureException(oolfe);
        }
        else if (ex instanceof GroupLeaderDoesNotExistException) {
            GroupLeaderDoesNotExistException gldnee = (GroupLeaderDoesNotExistException) ex;
            return handleGroupLeaderDoesNotExistException(gldnee);
        }
        else if (ex instanceof ProjectStatusDoesNotExistException) {
            ProjectStatusDoesNotExistException psdnee = (ProjectStatusDoesNotExistException) ex;
            return handleProjectStatusDoesNotExistException(psdnee);
        }
        else if (ex instanceof StartDateIsAfterEndDateException) {
            StartDateIsAfterEndDateException sdiaede = (StartDateIsAfterEndDateException) ex;
            return handleStartDateIsAfterEndDateException(sdiaede);
        }
        else if (ex instanceof DeletedProjectStatusNotNewException) {
            DeletedProjectStatusNotNewException dpsnne = (DeletedProjectStatusNotNewException) ex;
            return handleDeletedProjectStatusNotNewException(dpsnne);
        }
        else if (ex instanceof ProjectNotFoundException) {
            ProjectNotFoundException pnfe = (ProjectNotFoundException) ex;
            return handleProjectNotFoundException(pnfe);
        }
        else {
            return handleInternalException(ex);
        }
    }

    private List<String> handleErrors(String... errors) {
        List<String> result = new ArrayList<>();
        Collections.addAll(result, errors);
        return result;
    }

    private ExceptionDto setupException(String title, String message, int statusCode) {
        ExceptionDto ex = new ExceptionDto();
        ex.setTitle(title);
        ex.setMessage(message);
        ex.setStatusCode(statusCode);
        return ex;
    }

    private ResponseEntity<ExceptionDto> handleProjectNumberAlreadyExistsException(ProjectNumberAlreadyExistsException pnaee) {
        String title = "PROJECT NUMBER EXISTED";
        ExceptionDto ex = setupException(title, pnaee.getMessage(), HttpStatus.BAD_REQUEST.value());
        ex.setErrors(handleErrors(pnaee.getCurrentProjectNumber().toString()));
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionDto> handleEmployeeVisaDoesNotExistException(EmployeeVisaDoesNotExistException evdnee) {
        String title = "INVALID VISAS";
        ExceptionDto ex = setupException(title, evdnee.getMessage(), HttpStatus.NOT_FOUND.value());
        ex.setErrors(handleErrors(evdnee.getInvalidVisas()));
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionDto> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException oolfe) {
        String title = "OPTIMISTIC LOCKING";
        ExceptionDto ex = setupException(title, oolfe.getMessage(), HttpStatus.CONFLICT.value());
        ex.setErrors(handleErrors(oolfe.getPersistentClassName()));
        return new ResponseEntity<>(ex, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ExceptionDto> handleGroupLeaderDoesNotExistException(GroupLeaderDoesNotExistException gldnee) {
        String title = "GROUP LEADER DOES NOT EXIST";
        ExceptionDto ex = setupException(title, gldnee.getMessage(), HttpStatus.NOT_FOUND.value());
        ex.setErrors(handleErrors(gldnee.getGroupLeaderVisa()));
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionDto> handleProjectStatusDoesNotExistException(ProjectStatusDoesNotExistException psdnee) {
        String title = "PROJECT STATUS DOES NOT EXIST";
        ExceptionDto ex = setupException(title, psdnee.getMessage(), HttpStatus.NOT_FOUND.value());
        ex.setErrors(handleErrors(psdnee.getStatus()));
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionDto> handleStartDateIsAfterEndDateException(StartDateIsAfterEndDateException sdiaede) {
        String title = "START DATE IS AFTER END DATE";
        ExceptionDto ex = setupException(title, sdiaede.getMessage(), HttpStatus.BAD_REQUEST.value());
        ex.setErrors(handleErrors(sdiaede.getStartDate().toString(), sdiaede.getEndDate().toString()));
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionDto> handleDeletedProjectStatusNotNewException(DeletedProjectStatusNotNewException dpsnne) {
        String title = "DELETED PROJECTS STATUS NOT NEW";
        ExceptionDto ex = setupException(title, dpsnne.getMessage(), HttpStatus.BAD_REQUEST.value());
        ex.setErrors(handleErrors(dpsnne.getProjectNames()));
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionDto> handleProjectNotFoundException(ProjectNotFoundException pnfe) {
        String title = "PROJECT NOT FOUND";
        ExceptionDto ex = setupException(title, pnfe.getMessage(), HttpStatus.NOT_FOUND.value());
        ex.setErrors(pnfe.getNotFoundIds().stream().map(String::valueOf).collect(Collectors.toList()));
        return new ResponseEntity<> (ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionDto> handleInternalException(Exception exception) {
        String title = "INTERNAL EXCEPTION";
        ExceptionDto ex = setupException(title, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
