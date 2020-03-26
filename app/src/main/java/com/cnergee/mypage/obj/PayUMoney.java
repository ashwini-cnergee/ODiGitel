package com.cnergee.mypage.obj;


public class PayUMoney
{
    private String Success_url;

    private String Amount;

    private String Hash_value;

    private String MerchantId;

    private String MerchantKey;

    private String Cust_Emailid;

    private String First_name;

    private String Trans_id;

    private String Failure_url;

    private String merchant_salt;

    private String Product_name;

    private String MobileNo;

    public String getSuccess_url ()
    {
        return Success_url;
    }

    public void setSuccess_url (String Success_url)
    {
        this.Success_url = Success_url;
    }

    public String getAmount ()
    {
        return Amount;
    }

    public void setAmount (String Amount)
    {
        this.Amount = Amount;
    }

    public String getHash_value ()
    {
        return Hash_value;
    }

    public void setHash_value (String Hash_value)
    {
        this.Hash_value = Hash_value;
    }

    public String getMerchantId ()
    {
        return MerchantId;
    }

    public void setMerchantId (String MerchantId)
    {
        this.MerchantId = MerchantId;
    }

    public String getCust_Emailid() {
        return Cust_Emailid;
    }

    public void setCust_Emailid(String cust_Emailid) {
        Cust_Emailid = cust_Emailid;
    }


    public String getFirst_name ()
    {
        return First_name;
    }

    public void setFirst_name (String First_name)
    {
        this.First_name = First_name;
    }

    public String getTrans_id ()
    {
        return Trans_id;
    }

    public void setTrans_id (String Trans_id)
    {
        this.Trans_id = Trans_id;
    }

    public String getFailure_url ()
    {
        return Failure_url;
    }

    public void setFailure_url (String Failure_url)
    {
        this.Failure_url = Failure_url;
    }

    public String getMerchant_salt ()
    {
        return merchant_salt;
    }

    public void setMerchant_salt (String merchant_salt)
    {
        this.merchant_salt = merchant_salt;
    }

    public String getProduct_name ()
    {
        return Product_name;
    }

    public void setProduct_name (String Product_name)
    {
        this.Product_name = Product_name;
    }

    public String getMobileNo ()
    {
        return MobileNo;
    }

    public void setMobileNo (String MobileNo)
    {
        this.MobileNo = MobileNo;
    }

    public String getMerchantKey() {
        return MerchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        MerchantKey = merchantKey;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Success_url = "+Success_url+", Amount = "+Amount+", Hash_value = "+Hash_value+", MerchantId = "+MerchantId+", Emailid = "+Cust_Emailid+",  First_name = "+First_name+", Trans_id = "+Trans_id+", Failure_url = "+Failure_url+", merchant_salt = "+merchant_salt+", Product_name = "+Product_name+", MobileNo = "+MobileNo+"]";
    }
}

			