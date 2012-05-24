package pheidip.logic;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public final class ValidationUtils
{
  public static <E> Set<ConstraintViolation<E>> validate(E instance)
  {
    ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    return validator.validate(instance);
  }
}
