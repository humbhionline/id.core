package in.succinct.id.core.extensions;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.Database;
import com.venky.swf.plugins.collab.db.model.config.City;
import com.venky.swf.plugins.collab.db.model.config.Country;
import com.venky.swf.plugins.collab.db.model.config.PinCode;
import com.venky.swf.plugins.collab.db.model.config.State;
import in.succinct.id.core.db.model.docs.SubmittedDocument;
import in.succinct.id.core.db.model.onboarding.company.Company;
import in.succinct.id.core.db.model.user.User;
import in.succinct.id.core.util.AadharEKyc;
import in.succinct.id.core.util.AadharEKyc.AadharData;
import in.succinct.plugins.kyc.db.model.VerifiableDocument;
import in.succinct.plugins.kyc.extensions.VerifiableDocumentExtension;

import java.sql.Date;

public class SubmittedDocumentExtension extends VerifiableDocumentExtension<SubmittedDocument> {
    static {
        registerExtension(new SubmittedDocumentExtension());
    }

    @Override
    public void beforeValidate(SubmittedDocument instance) {
        if (instance.getDocumentedModelId() == null){
            if (instance.getCompanyId() != null){
                instance.setDocumentedModelName(Company.class.getSimpleName());
                instance.setDocumentedModelId(instance.getCompanyId());
            }else if (instance.getUserId() != null){
                instance.setDocumentedModelName(User.class.getSimpleName());
                instance.setDocumentedModelId(instance.getUserId());
            }
            SubmittedDocument tmp = Database.getTable(SubmittedDocument.class).getRefreshed(instance);
            if (!tmp.getRawRecord().isNewRecord()) {
                instance.setRawRecord(tmp.getRawRecord());
            }
        }else {
            if (ObjectUtil.equals("Company", instance.getDocumentedModelName())) {
                instance.setCompanyId(instance.getDocumentedModelId());
                instance.setUserId(null);
            } else if (ObjectUtil.equals("User", instance.getDocumentedModelName())) {
                instance.setUserId(instance.getDocumentedModelId());
                instance.setCompanyId(null);
            }
        }
        super.beforeValidate(instance);

        if (instance.getUserId() == null){
            return;
        }
        User user = instance.getUser();

        if (instance.getDocument().getDocumentName().equals("AADHAR")){
            if (!instance.getVerificationStatus().equals(VerifiableDocument.APPROVED) && instance.getFileContentSize() > 0 && instance.getFileContentName().endsWith(".zip")){
                try {
                    AadharData data = AadharEKyc.getInstance().parseZip(instance.getFile(),instance.getPassword());
                    if (data != null){
                        if (!ObjectUtil.isVoid(user.getPhoneNumber()) ){
                            user.setPhoneNumberVerified(data.isValidPhone(user.getPhoneNumber()));
                        }

                        if (!ObjectUtil.isVoid(user.getEmail())){
                            user.setEmailVerified(data.isValidEmail(user.getEmail()));
                        }

                        user.setLongName(data.get(AadharEKyc.AadharData.NAME));
                        user.setDateOfBirth(new Date(data.getDateOfBirth().getTime()));
                        user.setDateOfBirthVerified(true);
                        user.setAddressLine1(data.get(AadharEKyc.AadharData.HOUSE));
                        user.setAddressLine2(data.get(AadharEKyc.AadharData.STREET));
                        user.setAddressLine3(data.get(AadharEKyc.AadharData.LOCALITY));
                        user.setAddressLine4(data.get(AadharEKyc.AadharData.POST_OFFICE));
                        user.setCountryId(Country.findByName("India").getId());
                        State state = State.findByCountryAndName(user.getCountryId(), data.get(AadharEKyc.AadharData.STATE));
                        if (state != null) {
                            user.setStateId(state.getId());
                        }
                        City city = City.findByStateAndName(user.getStateId(), data.get(AadharEKyc.AadharData.DISTRICT));
                        if (city == null) {
                            city = City.findByStateAndName(user.getStateId(), data.get(AadharEKyc.AadharData.LOCALITY));
                        }
                        if (city != null) {
                            user.setCityId(city.getId());
                        }


                        PinCode pinCode = PinCode.find(data.get(AadharEKyc.AadharData.PIN_CODE));
                        if (pinCode != null) {
                            user.setPinCodeId(pinCode.getId());
                        }

                        user.setAddressVerified(true);
                        user.setTxnProperty("being.verified",true);
                        user.save();
                        instance.setVerificationStatus(VerifiableDocument.APPROVED);
                        instance.setTxnProperty("being.verified",true);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                    //Config.instance().getLogger(getClass().getName()).log(Level.WARNING,"Failed Aadhar Validation", e);
                }
            }
        }
    }

}
