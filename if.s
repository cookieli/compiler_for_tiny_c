.file "tests/codegen-hidden/input/hidden-31-scope-shadowing.dcf"
.text
.comm	x,8, 8
.section .rodata
.LC0:
.string "x should be 0; x = %d\n"
.LC1:
.string "Sum should be 10; x + y = %d\n"
.LC2:
.string "This shouldnt print\n"
.LC3:
.string "We have mastered shadowing\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $0,%rax
movq %rax,-16(%rbp)
movq %rax,-8(%rbp)
movq $7,x(%rip)
movb $1,-1(%rbp)
movb -1(%rbp),%al
cmpb $0,%al
jle .L2
subq $16, %rsp
movq $0,%rax
movq %rax,(%rsp)
movq %rax,8(%rsp)
movq -9(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $16, %rsp
jmp .L2
.L2:
movb -1(%rbp),%al
cmpb $0,%al
jle .L3
subq $16, %rsp
movq $0,%rax
movq %rax,(%rsp)
movq %rax,8(%rsp)
movq $3,-17(%rbp)
movq x(%rip),%rax
movq -17(%rbp),%r10
addq %r10,%rax
movq %rax,-9(%rbp)
movq -9(%rbp),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $16, %rsp
jmp .L3
.L3:
movb -1(%rbp),%al
cmpb $0,%al
jle .L1
subq $16, %rsp
movq $0,%rax
movq %rax,(%rsp)
movq %rax,8(%rsp)
movb -2(%rbp),%al
cmpb $0,%al
jle .L4
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L5
.L4:
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L5:
addq $16, %rsp
jmp .L1
.L1:
leave
ret

