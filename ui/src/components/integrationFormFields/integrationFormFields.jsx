import React, { useEffect } from 'react';
import { ATTRIBUTES, EVENT_ATTRIBUTES, LABELS, EVENT_LABELS } from 'components/constants';

export const IntegrationFormFields = (props) => {
  const { initialize, disabled, lineAlign, initialData, shown, ...extensionProps } = props;
  const {
    // TODO: share it from the core via WMF
    components: { IntegrationFormField, FieldErrorHint, Input, FieldProvider, Checkbox },
    validators: { requiredField },
  } = extensionProps;
  useEffect(() => {
    initialize(initialData);
  }, []);

  return (
    <>
      <IntegrationFormField
        name={ATTRIBUTES.INTEGRATION_NAME}
        label={LABELS.INTEGRATION_NAME}
        validate={requiredField}
        disabled={disabled}
        isRequired
      >
        <FieldErrorHint>
          <Input mobileDisabled />
        </FieldErrorHint>
      </IntegrationFormField>
      <IntegrationFormField
        name={ATTRIBUTES.CHANNEL}
        label={LABELS.CHANNEL}
        validate={requiredField}
        disabled={disabled}
        isRequired
      >
        <FieldErrorHint>
          <Input mobileDisabled />
        </FieldErrorHint>
      </IntegrationFormField>
      <IntegrationFormField
        name={ATTRIBUTES.TOKEN}
        label={LABELS.TOKEN}
        validate={requiredField}
        disabled={disabled}
        isRequired
      >
        <FieldErrorHint>
          <Input mobileDisabled />
        </FieldErrorHint>
      </IntegrationFormField>
      <IntegrationFormField
        name={ATTRIBUTES.APP_URL}
        label={LABELS.APP_URL}
        validate={requiredField}
        disabled={disabled}
        isRequired
      >
        <FieldErrorHint>
          <Input placeholder={window.location.origin} mobileDisabled />
        </FieldErrorHint>
      </IntegrationFormField>
      <IntegrationFormField
        name={EVENT_ATTRIBUTES.LAUNCH_STARTED}
        disabled={disabled}
      >
        <FieldProvider disabled={disabled} format={Boolean}>
          <Checkbox>{EVENT_LABELS.LAUNCH_STARTED}</Checkbox>
        </FieldProvider>
      </IntegrationFormField>
      <IntegrationFormField
        name={EVENT_ATTRIBUTES.LAUNCH_FINISHED}
        disabled={disabled}
      >
        <FieldProvider disabled={disabled} format={Boolean}>
          <Checkbox>{EVENT_LABELS.LAUNCH_FINISHED}</Checkbox>
        </FieldProvider>
      </IntegrationFormField>
      <IntegrationFormField
        name={EVENT_ATTRIBUTES.AUTO_ANALYSIS_FINISHED}
        disabled={disabled}
      >
        <FieldProvider disabled={disabled} format={Boolean}>
          <Checkbox>{EVENT_LABELS.AUTO_ANALYSIS_FINISHED}</Checkbox>
        </FieldProvider>
      </IntegrationFormField>
      <IntegrationFormField
        name={EVENT_ATTRIBUTES.UNIQUE_ERROR_ANALYSIS_STARTED}
        disabled={disabled}
      >
        <FieldProvider disabled={disabled} format={Boolean}>
          <Checkbox>{EVENT_LABELS.UNIQUE_ERROR_ANALYSIS_STARTED}</Checkbox>
        </FieldProvider>
      </IntegrationFormField>
      <IntegrationFormField
        name={EVENT_ATTRIBUTES.UNIQUE_ERROR_ANALYSIS_FINISHED}
        disabled={disabled}
      >
        <FieldProvider disabled={disabled} format={Boolean}>
          <Checkbox>{EVENT_LABELS.UNIQUE_ERROR_ANALYSIS_FINISHED}</Checkbox>
        </FieldProvider>
      </IntegrationFormField>
    </>
  );
};
