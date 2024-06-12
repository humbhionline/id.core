package in.succinct.id.core.extensions;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.extensions.ParticipantExtension;
import com.venky.swf.db.model.User;
import in.succinct.id.core.db.model.docs.SubmittedDocument;

import java.util.Collections;
import java.util.List;

public class SubmittedDocumentParticipantExtension extends ParticipantExtension<SubmittedDocument> {
    static {
        registerExtension(new SubmittedDocumentParticipantExtension());
    }
    @Override
    public List<Long> getAllowedFieldValues(User user, SubmittedDocument partiallyFilledModel, String fieldName) {
        com.venky.swf.plugins.collab.db.model.user.User u = user.getRawRecord().getAsProxy(com.venky.swf.plugins.collab.db.model.user.User.class);
        if (ObjectUtil.equals(fieldName,"USER_ID")) {
            if (u.isStaff()) {
                return null;
            } else {
                return Collections.singletonList(u.getId());
            }
        }else if (ObjectUtil.equals(fieldName,"COMPANY_ID")){
            return u.getCompanyIds();
        }
        return null;
    }
}
