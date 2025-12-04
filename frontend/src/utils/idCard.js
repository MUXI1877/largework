export const parseIdCard = (idCard) => {
  if (!idCard || idCard.length !== 18) {
    return null
  }
  
  const genderCode = parseInt(idCard.substring(16, 17))
  const gender = genderCode % 2 === 0 ? '女' : '男'
  
  const birthStr = idCard.substring(6, 14)
  const year = birthStr.substring(0, 4)
  const month = birthStr.substring(4, 6)
  const day = birthStr.substring(6, 8)
  const birthDate = `${year}-${month}-${day}`
  
  return { gender, birthDate }
}

export const validateIdCard = (idCard) => {
  const pattern = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/
  return pattern.test(idCard)
}

