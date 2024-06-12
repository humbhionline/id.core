package in.succinct.id.core.configuration;

import com.venky.swf.configuration.Installer;
import com.venky.swf.db.Database;
import com.venky.swf.db.model.application.api.OpenApi;
import com.venky.swf.plugins.collab.db.model.config.Role;
import com.venky.swf.sql.Conjunction;
import com.venky.swf.sql.Expression;
import com.venky.swf.sql.Operator;
import com.venky.swf.sql.Select;
import in.succinct.id.core.db.model.DefaultUserRoles;
import in.succinct.id.core.db.model.onboarding.company.Company;
import in.succinct.id.core.db.model.user.User;
import in.succinct.plugins.kyc.db.model.submissions.Document;
import in.succinct.plugins.kyc.db.model.submissions.KycGroup;
import in.succinct.plugins.kyc.db.model.submissions.SubmittedDocument;

import java.util.List;

public class AppInstaller implements Installer {

    public void install() {
        installRoles();
        installDocumentTypes();
        migrateSubmittedDocuments();
    }

    private void migrateSubmittedDocuments() {

        Select select = new Select().from(SubmittedDocument.class);
        select.where(new Expression(select.getPool(), Conjunction.AND).
                add(new Expression(select.getPool(), "USER_ID", Operator.EQ)).
                add(new Expression(select.getPool(), "COMPANY_ID", Operator.EQ)));
        List<SubmittedDocument> documentList = select.execute();
        documentList.forEach(document -> {
            document.setRemarks("Migrate User / Company");
            document.save();
        });

    }


    public void installRoles() {
        if (Database.getTable(Role.class).isEmpty()) {
            for (String allowedRole : DefaultUserRoles.ALLOWED_ROLES) {
                Role role = Database.getTable(Role.class).newRecord();
                role.setName(allowedRole);
                role.save();
            }
        }
        Role admin = com.venky.swf.plugins.security.db.model.Role.getRole(Role.class, "ADMIN");
        assert admin != null;
        if (!admin.isStaff()) {
            admin.setStaff(true);
            admin.save();
        }
    }

    public void installDocumentTypes() {
        if (!new Select().from(KycGroup.class).execute(1).isEmpty()) {
            return;
        }
        KycGroup idProof = Database.getTable(KycGroup.class).newRecord();
        idProof.setName("ID_PROOF");
        idProof = Database.getTable(KycGroup.class).getRefreshed(idProof);
        idProof.setMinDocumentsNeeded(1);
        idProof.save();

        KycGroup kyb = Database.getTable(KycGroup.class).newRecord();
        kyb.setName("KYB");
        kyb = Database.getTable(KycGroup.class).getRefreshed(kyb);
        kyb.setMinDocumentsNeeded(1);
        kyb.save();

        for (String defaultDocumentType : User.DEFAULT_DOCUMENTS) {
            Document documentType = Database.getTable(Document.class).newRecord();
            documentType.setDocumentName(defaultDocumentType);
            documentType.setDocumentedModelName(User.class.getSimpleName());
            documentType.setKycGroupId(idProof.getId());
            documentType = Database.getTable(Document.class).getRefreshed(documentType);
            documentType.save();
        }
        for (String defaultDocumentType : Company.DEFAULT_DOCUMENTS) {
            Document documentType = Database.getTable(Document.class).newRecord();
            documentType.setDocumentName(defaultDocumentType);
            documentType.setDocumentedModelName(Company.class.getSimpleName());
            documentType.setKycGroupId(kyb.getId());
            documentType = Database.getTable(Document.class).getRefreshed(documentType);
            documentType.save();
        }


    }

}

